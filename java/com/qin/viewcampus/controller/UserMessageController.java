package com.qin.viewcampus.controller;

import com.qin.viewcampus.service.IUserMessageService;
import com.qin.viewcampus.util.TransportSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class UserMessageController {

    @Autowired
    private IUserMessageService iUserMessageService;

    //查看用户在某消息表内的所有消息
    @GetMapping("/user/{id}")
    public TransportSpecification GetAllUserMessageById(@PathVariable String id){
        return new TransportSpecification(true,iUserMessageService.GetUserMessageById(id));
    }
}
