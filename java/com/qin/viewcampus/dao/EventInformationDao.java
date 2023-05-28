package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.EventInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EventInformationDao extends BaseMapper<EventInformation> {

    @Select("select event_site from event_informations where event_id = #{id}")
    public String GetSite(String id);
}
