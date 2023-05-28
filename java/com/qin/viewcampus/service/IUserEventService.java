package com.qin.viewcampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.viewcampus.entity.UserEvent;

import java.util.List;

public interface IUserEventService extends IService<UserEvent> {

    //通过用户id查询用户的活动id集
    public List<String> EventsIdOfUser(String id);
    //通过用户id查找用户并为之更新id集
    Boolean UpdateEventIdById(String id, String eid);
}
