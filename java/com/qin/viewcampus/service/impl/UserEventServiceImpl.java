package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.viewcampus.dao.UserEventDao;
import com.qin.viewcampus.entity.UserEvent;
import com.qin.viewcampus.service.IUserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UserEventServiceImpl extends ServiceImpl<UserEventDao, UserEvent> implements IUserEventService {

    @Autowired
    private UserEventDao userEventDao;

    //通过用户id查询用户的活动id集
    @Override
    public List<String> EventsIdOfUser(String id) {
        List<String> idList = Arrays.asList(userEventDao.selectById(id).getEventsId().split(","));
        return idList;
    }

    //通过用户id查找用户并为之id集添加新的活动id
    @Override
    public Boolean UpdateEventIdById(String uid, String eid) {
        List<String> idList = new ArrayList<>(EventsIdOfUser(uid));
        if(Objects.equals(idList.get(0), "") || idList.get(0)==null){
            idList.set(0,eid);
        }else {
            idList.add(eid);
        }
        String updateEventId = String.join(",",idList);
        return userEventDao.updateById(new UserEvent(uid,updateEventId))==1;
    }

    //通过id查找用户并为之更新id集
}
