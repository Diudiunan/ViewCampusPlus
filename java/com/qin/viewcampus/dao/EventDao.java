package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.Event;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EventDao extends BaseMapper<Event> {

    //获得处于激活态的活动
    @Select("select event_id from events where status_approval = 1 and status_end = 0")
    public List<String> GetActiveEvents();

}
