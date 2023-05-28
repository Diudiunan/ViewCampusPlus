package com.qin.viewcampus.controller;

import com.qin.viewcampus.entity.UserInformation;
import com.qin.viewcampus.service.IUserInformationService;
import com.qin.viewcampus.util.TransportSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userinformation")
public class UserInformationController {

    @Autowired
    private IUserInformationService iUserInformationService;

    //通关id获取用户信息
    @GetMapping("/{id}")
    public TransportSpecification GetUserInformation(@PathVariable String id){
        return new TransportSpecification(true,iUserInformationService.getById(id));
    }

    //添加用户信息
    @PostMapping
    public TransportSpecification SaveUserInformation(@RequestBody UserInformation userInformation){
        return new TransportSpecification(iUserInformationService.save(userInformation));
    }

    //通过id修改用户信息
    @PutMapping
    public TransportSpecification UpdateUserInformation(@RequestBody UserInformation userInformation){
        return new TransportSpecification(iUserInformationService.updateById(userInformation));
    }

    /* 将此功能归到UserController的DeleteEventById中
    //通过id删除用户信息
    @DeleteMapping
    public TransportSpecification DeleteUserInformationById(@PathVariable String id){
        return new TransportSpecification((iUserInformationService.removeById(id)));
    }
     */
}
