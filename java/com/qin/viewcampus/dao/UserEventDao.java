package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.UserEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEventDao extends BaseMapper<UserEvent> {

    //创建用户
    @Insert("insert into user_events(user_id, events_id) values (#{id}, #{ids})")
    public Boolean InsertUserEvents(String id, String ids);
}
