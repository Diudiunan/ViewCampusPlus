package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao extends BaseMapper<User> {

    //创建用户
    @Insert("insert into users( user_id, user_account, user_pass, user_right) values " +
            "(#{id}, #{account}, #{pass}, #{right})")
    public Boolean InsertUser(String id, String account, Integer pass, Integer right);
    //通过id查询用户是否存在
    @Select("select exists (select user_id from users where user_id = #{id})")
    public Integer SelectUserIsExist(String id);
}
