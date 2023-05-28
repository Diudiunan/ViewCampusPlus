package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qin.viewcampus.dao.*;
import com.qin.viewcampus.entity.*;
import com.qin.viewcampus.service.IUserService;
import com.qin.viewcampus.util.UserProof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.qin.viewcampus.util.EventConstants.*;
import static com.qin.viewcampus.util.toRedis.RedisEventConstants.Event_Activated_IdSet_key;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserEventDao userEventDao;
    @Autowired
    private EventDao eventDao;
    @Autowired
    private EventInformationDao eventInformationDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    //通过id删除user，包括了users表和user_information表的信息
    @Override
    public Boolean DeleteUserById(String id) {
        return userDao.deleteById(id)==userInformationDao.deleteById(id)==true;
    }

    //创建新用户，包括了users表、user_information表和user_events表的信息
    @Override
    public Boolean SaveUser(UserProof userProof) {
        String id = userProof.getOpenid();
        String name = userProof.getName();
        if(UserIsExist(id)){
            return false;
        }else {
            User user = new User(id,"",1,1);
            UserInformation userInformation = new UserInformation(id,name,"","");
            UserEvent userEvent = new UserEvent(id,"");
            boolean iUser =  userDao.insert(user) ==1;
            boolean iUserInformation = userInformationDao.insert(userInformation) == 1;
            boolean iUserEvent = userEventDao.insert(userEvent) ==1;
            //Boolean iUser =  userDao.InsertUser(id,"1925050999",1,1);
            //Boolean iUserInformation = userInformationDao.InsertUserInformation(id,name,"","");
            //Boolean iUserEvent = userEventDao.InsertUserEvents(id,"");
            return iUser==iUserInformation==iUserEvent==true;
        }
    }

    //通过id查询用户是否存在
    @Override
    public Boolean UserIsExist(String id) {
        return userDao.SelectUserIsExist(id)>0;
    }


    //以活动id集和激活态为依据获取用户的event的所有信息
    @Override
    public List<ArrayList<Object>> StateEventsOfUser(List<String> idList, Boolean status) {
        List<ArrayList<Object>> eventListOfUser = new ArrayList<>();
        List<String> newList = new ArrayList<>(idList);
        //获得redis内的激活id集合
        Set<String> redisSet = stringRedisTemplate.opsForSet().members(Event_Activated_IdSet_key);
        //断言
        assert redisSet != null;
        //获得操作集合(激活活动集或失活活动集)
        if(status){
            //获得用户的激活活动集
            newList.retainAll(redisSet);
        }else {
            //获得用户的失活活动集
            redisSet.forEach(newList::remove);
        }
        //获取数据
        for(String id: newList){
            ArrayList<Object> al = new ArrayList<>();
            Event event = eventDao.selectById(id);
            EventInformation eventInformation = eventInformationDao.selectById(id);
            if(null == event|| null == eventInformation){
                break;
            }
            al.add(event);
            al.add(eventInformation);
            eventListOfUser.add(al);
        }
        //返回要求的活动列表
        return eventListOfUser;
    }
}
