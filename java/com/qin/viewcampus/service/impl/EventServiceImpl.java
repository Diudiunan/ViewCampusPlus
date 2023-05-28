package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.viewcampus.dao.EventDao;
import com.qin.viewcampus.dao.EventInformationDao;
import com.qin.viewcampus.entity.Event;
import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.service.IEventInformationService;
import com.qin.viewcampus.service.IEventService;
import com.qin.viewcampus.service.IUserMessageService;
import com.qin.viewcampus.util.EventAuditBySystem;
import com.qin.viewcampus.util.EventIdFactory;
import com.qin.viewcampus.util.EventRecommend;
import com.qin.viewcampus.util.EventsToMarkers;
import com.qin.viewcampus.util.toRedis.RedisMarkerConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;

import static com.qin.viewcampus.util.EventConstants.*;
import static com.qin.viewcampus.util.toRedis.RedisEventConstants.*;
import static com.qin.viewcampus.util.toRedis.RedisMarkerConstants.*;

@Service
public class EventServiceImpl extends ServiceImpl<EventDao, Event> implements IEventService {

    @Autowired
    private EventDao eventDao;
    @Autowired
    private EventInformationDao eventInformationDao;
    @Autowired
    private IEventInformationService iEventInformationService;
    @Autowired
    private IUserMessageService IMS;
    @Autowired
    private UserEventServiceImpl userEventService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //搜索id并返回此活动的所有信息
    @Override
    public ArrayList<Object> GetSearchResultById(String id){
        //获得redis内的id集合
        Set<String> redisSet = stringRedisTemplate.opsForSet().members(Event_Activated_IdSet_key);
        if(redisSet.contains(id)){
            return GetEventAllInformation(id);
        }
        else {
            return null;
        }
    }

    //通过id获得event的所有信息
    @Override
    public ArrayList<Object> GetEventAllInformation(String id){
        //获得某活动在redis内的信息
        Map<Object, Object> eventMap = stringRedisTemplate.opsForHash().entries(Event_key+id);
        ArrayList<Object> eventList= new ArrayList<>();
        //判断该活动是否在redis中
        if (eventMap.isEmpty()){
            //返回MySQL内的最新数据
            eventList =  PutEventToRedis(id);
        }
        //在redis内查找活动
        else{
            try {
                String stringEvent = (String) eventMap.get(Event_event);
                String stringEventInformation = (String) eventMap.get(Event_eventInformation);
                Event event = objectMapper.readValue(stringEvent, Event.class);
                EventInformation eventInformation = objectMapper.readValue(stringEventInformation, EventInformation.class);
                eventList.add(event);
                eventList.add(eventInformation);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return eventList;
    }

    //通过id删除event，包括了events表和event_information表的信息
    @Override
    public Boolean DeleteEventById(String id) {
        return eventDao.deleteById(id)== eventInformationDao.deleteById(id)==true;
    }

    //保存活动，向events和event_informations表添加信息
    @Override
    public Boolean SaveEvent(Event event, EventInformation eventInformation, String uid){
        //活动工厂
        Callable<String> callableId =  new EventIdFactory();
        FutureTask<String> futureTaskId = new FutureTask<>(callableId);
        Thread threadId = new Thread(futureTaskId);
        threadId.start();
        //活动审查
        Callable<Integer> callableAp = new EventAuditBySystem(uid, eventInformation, event);
        FutureTask<Integer> futureTaskAp = new FutureTask<>(callableAp);
        Thread threadAp = new Thread(futureTaskAp);
        threadAp.start();
        try {
            String eid = futureTaskId.get();
            Integer pass = futureTaskAp.get();
            event.setEventId(eid);
            eventInformation.setEventId(eid);
            //是否通过
            event.setStatusApproval(pass);
            event.setStatusEnd(0);
            //添加和更新
            boolean iEvent = eventDao.insert(event) ==1;
            boolean iEventInformation = eventInformationDao.insert(eventInformation) ==1;
            Boolean updateUserEvent = userEventService.UpdateEventIdById(uid,eid);
            return iEvent == iEventInformation == updateUserEvent;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    //从events表中获取一些处于激活状态的event并将数据转为marker形式为前端提供服务
    @Override
    public List<EventsToMarkers> GetActiveMarkers(Integer filter) {
        List<EventsToMarkers> mList = new ArrayList<>();
        Set<String> markerMap = stringRedisTemplate.opsForSet().members(Marker_Event_key+filter);
        //如果redis内部没有markers集合
        assert markerMap != null;
        if(markerMap.isEmpty()){
            System.out.println(filter);
            mList = PutMarkerToRedis(filter);
        }
        //如果redis内部已有markers集合
        else {
            for(String str: markerMap){
                try {
                    EventsToMarkers marker = objectMapper.readValue(str,EventsToMarkers.class);
                    mList.add(marker);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return mList;
    }

    //获得所有已激活的event
    @Override
    public List<Event> GetActiveEvents() {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Event::getEventId,Event::getEventName,Event::getEventSponsor)
                .eq(Event::getStatusApproval,Approval_True)
                .eq(Event::getStatusEnd,End_False);
        return eventDao.selectList(queryWrapper);
    }

    //获得所有失活的event
    @Override
    public List<Event> GetOffEvents() {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Event::getEventId,Event::getEventName,Event::getEventSponsor)
                .eq(Event::getStatusApproval,Approval_True)
                .eq(Event::getStatusEnd,End_True);
        return eventDao.selectList(queryWrapper);
    }

    //获得被推荐的已激活的活动
    @Override
    public List<Event> GetRecommendActiveEvents(int currentpage, int pagesize, int recommend) {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .select("event_id", "event_name", "event_sponsor","event_grade")
                .eq("status_approval",1)
                .eq("status_end",0)
                //起始坐标(currentpage-1)*pagesize  结点坐标 currentpage*pagesize
                .last("limit "+pagesize+" offset "+(currentpage-1)*pagesize);
        List<Event> re_list = new EventRecommend(eventDao.selectList(queryWrapper), recommend).GetRecommendList();
        while(re_list.size()<pagesize){
            currentpage++;
            queryWrapper.clear();
            queryWrapper
                    .select("event_id", "event_name", "event_sponsor","event_grade")
                    .eq("status_approval",1)
                    .eq("status_end",0)
                    .last("limit "+pagesize+" offset "+(currentpage-1)*pagesize);
            List<Event> repeatList = new EventRecommend(eventDao.selectList(queryWrapper), recommend).GetRecommendList();
            if(repeatList.size()==0){
                break;
            }else{
                re_list.addAll(repeatList);
            }
        }
        return re_list;
    }

    //按要求获得分页，包括激活态的和失效态的活动
    @Override
    public IPage<Event> GetPage(int currentpage, int pagesize) {
        IPage page = new Page(currentpage, pagesize);
        eventDao.selectPage(page,null);
        return page;
    }

    //实时更新活动状态、redis缓存的markers
    //@PostConstruct
    @Async
    @Scheduled(cron = "0 0/1 * * * *")
    @Override
    public void UpdateEvent() {
        //更新激活态活动状态
        if (true){
            List<String> idList = eventDao.GetActiveEvents();
            LambdaQueryWrapper<EventInformation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(EventInformation::getEventId,idList);
            List<EventInformation> eventI_list =  eventInformationDao.selectList(queryWrapper);
            LambdaUpdateWrapper<Event> updateWrapper = new LambdaUpdateWrapper<>();
            for(EventInformation eventI: eventI_list){
                LocalDate ld = LocalDate.now();
                LocalTime lt = LocalTime.now();
                String re = ld+" "+lt;
                if(re.substring(0,19).compareTo(eventI.getEventEnd())>=1){
                    updateWrapper.set(Event::getStatusEnd,End_True);
                    updateWrapper.eq(Event::getEventId,eventI.getEventId());
                    eventDao.update(null,updateWrapper);
                    updateWrapper.clear();
                }
            }
        }
        System.out.println("活动状态更新一次");
        //更新缓存内markers内容
        Class<RedisMarkerConstants> c = RedisMarkerConstants.class;
        Field[] fields = c.getDeclaredFields();
        for(Field field: fields) {
            String str = field.getName();
            if (str.endsWith("Mode")) {
                try {
                    PutMarkerToRedis((Integer) field.get(str));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("地图markers缓存更新一次");
        if (UpdateRedisActivatedEventIdSet()){
            System.out.println("活动id集缓存更新一次");
        }else {
            System.out.println("活动id集缓存更新失败");
        }
    }

    //向redis更新一个event的全部信息
    @NotNull
    private ArrayList<Object> PutEventToRedis(String id){
        ArrayList<Object> eventList= new ArrayList<>();
        //查询MySQL，获得两个对象数据
        Event event = eventDao.selectById(id);
        EventInformation eventInformation = eventInformationDao.selectById(id);
        eventList.add(event);
        eventList.add(eventInformation);
        //将数据以hash形式存入redis
        Map<String, String> map = new HashMap<>();
        String eventStr = null;
        String eventIStr = null;
        //序列化对象
        try {
            eventStr = objectMapper.writeValueAsString(event);
            eventIStr = objectMapper.writeValueAsString(eventInformation);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        map.put(Event_event,eventStr);
        map.put(Event_eventInformation,eventIStr);
        stringRedisTemplate.opsForHash().putAll(Event_key+id,map);
        stringRedisTemplate.expire(Event_key+id,Event_Activated_TTL,TimeUnit.MINUTES);
        return eventList;
    }

    //向redis添加不同过滤模式的markers
    @NotNull
    private List<EventsToMarkers> PutMarkerToRedis(Integer filter){
        List<EventsToMarkers> mList = new ArrayList<>();
        //创建用于查找event信息的wrapper
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .select(Event::getEventId, Event::getEventName, Event::getEventSponsor)
                .eq(Event::getStatusApproval,Approval_True)
                .eq(Event::getStatusEnd,End_False);
        //添加过滤模式
        if(filter != Marker_Event_defaultMode){
            queryWrapper.eq(Event::getEventGrade,filter);
        }
        List<Event> eList = eventDao.selectList(queryWrapper);
        //获得id列表
        List<String> sList = new ArrayList<>();
        for(Event event: eList){
            sList.add(event.getEventId());
        }
        //创建用于查找eventInformation的wrapper
        LambdaQueryWrapper<EventInformation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .select(EventInformation::getEventSite)
                .in(!ObjectUtils.isEmpty(sList),EventInformation::getEventId,sList);
        List<EventInformation> eiList = eventInformationDao.selectList(lambdaQueryWrapper);
        //生成marker列表
        int count = 0;
        for (Event event: eList){
            EventsToMarkers eventsToMarkers = new EventsToMarkers();
            eventsToMarkers.setEventId(event.getEventId());
            eventsToMarkers.setEventName(event.getEventName());
            eventsToMarkers.setEventSponsor(event.getEventSponsor());
            //获得坐标数组
            String[] site = eiList.get(count).getEventSite().split(",");
            eventsToMarkers.setEventLongitude(site[1]);
            eventsToMarkers.setEventLatitude(site[0]);
            mList.add(eventsToMarkers);
            count++;
        }
        List<String> msList = new ArrayList<>();
        //序列化数据
        for(EventsToMarkers markers: mList){
            try {
                msList.add(objectMapper.writeValueAsString(markers));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //将数据存入redis
        for(String str: msList){
            stringRedisTemplate.opsForSet().add(Marker_Event_key+filter,str);
        }
        stringRedisTemplate.expire(Marker_Event_key+filter,Marker_Event_TTL,TimeUnit.MINUTES);
        return mList;
    }

    /**
     * 已激活活动id集合存入redis缓存
     */
    private boolean UpdateRedisActivatedEventIdSet(){
        //获得redis内的id集合
        Set<String> redisSet = stringRedisTemplate.opsForSet().members(Event_Activated_IdSet_key);
        //获取MySQL内的id集合
        List<String> sList = eventDao.GetActiveEvents();
        //更新数据
        Set<String> newSet = new HashSet<>();
        assert redisSet != null;
        Set<String> toolSet1 = new HashSet<>(redisSet);
        Set<String> toolSet2 = new HashSet<>(sList);
        //获得失效的活动id集
        sList.forEach(toolSet1::remove);
        for(String str: toolSet1){
            stringRedisTemplate.opsForSet().remove(Event_Activated_IdSet_key,str);
        }
        //活动新增的激活的活动id集
        redisSet.forEach(toolSet2::remove);
        for (String str: toolSet2){
            stringRedisTemplate.opsForSet().add(Event_Activated_IdSet_key,str);
        }
        //设置缓存时间
        stringRedisTemplate.expire(Event_Activated_IdSet_key,Events_Activated_IdSet_TTL,TimeUnit.MINUTES);
        return true;
    }
}
