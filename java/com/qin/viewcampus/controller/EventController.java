package com.qin.viewcampus.controller;

import com.qin.viewcampus.entity.Event;
import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.service.IEventService;
import com.qin.viewcampus.service.IUserEventService;
import com.qin.viewcampus.util.TransportSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private IEventService iEventService;

    //查看所有的event，包括激活态的和失效态的活动
    @GetMapping("/all")
    public TransportSpecification GetAllEvents(){
        return new TransportSpecification(true,iEventService.list());
    }

    //获得所有处于激活态的活动
    @GetMapping
    public TransportSpecification GetAllActiveEvents(){
        return new TransportSpecification(true,iEventService.GetActiveEvents());
    }

    //获得所有处于失活态的活动
    @GetMapping("/off")
    public TransportSpecification GetAllOffEvents(){
        return new TransportSpecification(true,iEventService.GetOffEvents());
    }

    //获得带所有处于激活态的活动并带有坐标
    @GetMapping("/markers/{filter}")
    public TransportSpecification GetSomeMarks(@PathVariable Integer filter){
        return new TransportSpecification(true,iEventService.GetActiveMarkers(filter));
    }

    //用于前端主页请求events的page分页展示，仅激活态活动
    @PostMapping ("/get")
    public TransportSpecification GetActivePage(@RequestBody Map<String, Integer> ma){
        int currentpage = ma.get("page");
        int pagesize = ma.get("number");
        int recommend = ma.get("recommend");
        return new TransportSpecification(true,iEventService.GetRecommendActiveEvents(currentpage, pagesize, recommend));
    }

    //用于前端主页请求events的page分页展示，包括激活态的和失效态的活动
    @GetMapping("/All/{currentpage}/{pagesize}")
    public TransportSpecification GetPage(@PathVariable int currentpage,@PathVariable int pagesize){
        return new TransportSpecification(true,iEventService.GetPage(currentpage, pagesize));
    }

    //按id查看event
    @GetMapping("/{id}")
    public TransportSpecification GetEventById(@PathVariable String id){
        return new TransportSpecification(true,iEventService.getById(id));
    }

    //EventInformationController同样有此功能
    //按id查看event的所有信息
    @GetMapping("/detail/{id}")
    public TransportSpecification GetDetailedEventById(@PathVariable String id){
        return new TransportSpecification(true,iEventService.GetEventAllInformation(id));
    }

    //查询活动,如果是则返回此活动所有信息
    @GetMapping("/search/{id}")
    public TransportSpecification GetSearchIdResult(@PathVariable String id){
        Object result = iEventService.GetSearchResultById(id);
        if(result==null){
            return new TransportSpecification(false,null);
        }
        else {
            return new TransportSpecification(true,result);
        }
    }

    //添加event
    //还要同时给event_informations和user_events添加
    @PostMapping("/save")
    public TransportSpecification SaveEvent(@RequestBody Map<String, String> ob){
        String uid = ob.get("userId");
        Event event = new Event();
        event.setEventName(ob.get("eventName"));
        event.setUserId(uid);
        event.setEventSponsor(ob.get("eventSponsor"));
        EventInformation eventInformation = new EventInformation();
        eventInformation.setEventContent(ob.get("eventContent"));
        eventInformation.setEventSite(ob.get("eventSite"));
        eventInformation.setEventStart(ob.get("eventStart"));
        eventInformation.setEventEnd(ob.get("eventEnd"));
        return new TransportSpecification(iEventService.SaveEvent(event, eventInformation, uid));
    }

    //修改event
    @PutMapping
    public TransportSpecification UpdateEvent(@RequestBody Event event){
        return new TransportSpecification(iEventService.updateById(event));
    }

    //按id删除event
    @DeleteMapping("/{id}")
    private TransportSpecification DeletEventById(@PathVariable String id){
        //使用自定义方法DeleteEventById删除event信息
        return new TransportSpecification(iEventService.DeleteEventById(id));
    }
}
