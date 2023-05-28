package com.qin.viewcampus.util;


import com.qin.viewcampus.entity.Event;
import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.service.IUserMessageService;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

//此类暂时存在无法调用service内部SetUserMessageById方法的问题
//将发送系统消息暂放在EventService中进行调用
//注释掉调用SetUserMessageById方法待日后改进
public class EventAuditBySystem implements Callable<Integer> {
    private String uid;
    private EventInformation EI;
    private Event E;
    private ApplicationContext ACT = GetBean.getApplicationContext();
    private IUserMessageService IMS = GetBean.getBean(IUserMessageService.class);

    public EventAuditBySystem(String uid, EventInformation EI, Event E) {
        this.uid = uid;
        this.EI = EI;
        this.E = E;
    }

    @Override
    public Integer call() throws Exception {
        Integer res = 1;
        String message = "请检查你的活动：'"+E.getEventName()+"'的状态";
        //异步发送系统消息
        Future<Boolean> sentMessage = IMS.SetUserMessageById(uid,res,message);
        Boolean isSuccess = sentMessage.get();
        System.out.println(isSuccess?"sent a message":"sent error");
        return res;
    }
}
