package com.qin.viewcampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.viewcampus.entity.UserMessage;

import java.util.List;
import java.util.concurrent.Future;

public interface IUserMessageService extends IService<UserMessage> {
    //返回用户id的位于某一数据表内的消息
    List<String> GetUserMessageById(String uid);
    //发给用户新的系统消息
    Future<Boolean> SetUserMessageById(String uid, Integer res, String message);
}
