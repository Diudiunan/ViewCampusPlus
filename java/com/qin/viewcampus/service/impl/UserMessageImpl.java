package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.viewcampus.dao.UserMessageDao;
import com.qin.viewcampus.entity.UserMessage;
import com.qin.viewcampus.service.IUserMessageService;
import com.qin.viewcampus.util.MessageIdFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class UserMessageImpl extends ServiceImpl<UserMessageDao, UserMessage> implements IUserMessageService {

    @Autowired
    private UserMessageDao userMessageDao;
    private MessageIdFactory messageIdFactory = new MessageIdFactory();

    //返回用户id的位于某一数据表内的消息
    @Override
    public List<String> GetUserMessageById(String uid) {
        QueryWrapper<UserMessage> wrapper = new QueryWrapper<>();
        wrapper.like("user_id",uid);
        List<UserMessage> userMessageList = userMessageDao.selectList(wrapper);
        List<String> stringList = new ArrayList<>();
        for(UserMessage userMessage: userMessageList){
            stringList.add(0,userMessage.getMessage());
        }
        return stringList;
    }

    //发送给用户新的系统消息，提示活动通过
    @Override
    @Async
    public Future<Boolean> SetUserMessageById(String uid, Integer res, String message) {
        String tableLabel = messageIdFactory.GetTableLabel();
        String mid = messageIdFactory.GetMID();
        String addMessage = res==1?"活动通过！":"活动未通过！";
        //Boolean resultOfSent = userMessageDao.insert(new UserMessage(mid,uid,addMessage+message))==1;
        Boolean resultOfSent = userMessageDao.SaveUserMessage("",mid,uid,addMessage+message)==1;
        return new AsyncResult<Boolean>(resultOfSent);
    }

}
