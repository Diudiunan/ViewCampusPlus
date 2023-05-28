package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.UserInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInformationDao extends BaseMapper<UserInformation> {

    //创建用户信息
    @Insert("insert into user_informations( user_id, user_name, user_introduction, user_unit) values " +
            "(#{id}, #{name}, #{introduction}, #{unit})")
    public Boolean InsertUserInformation(String id, String name, String introduction, String unit);
}
