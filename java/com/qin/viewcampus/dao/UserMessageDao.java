package com.qin.viewcampus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qin.viewcampus.entity.UserMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMessageDao extends BaseMapper<UserMessage> {

    //保存用户的系统消息

    @Insert("insert into user_message${tableLabel} (message_id, user_id, message) values (#{mid}, #{uid}, #{message})")
    public Integer SaveUserMessage(String tableLabel,String mid, String uid, String message);
}
