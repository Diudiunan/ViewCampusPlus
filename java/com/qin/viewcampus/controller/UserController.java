package com.qin.viewcampus.controller;

import com.qin.viewcampus.entity.User;
import com.qin.viewcampus.service.IUserEventService;
import com.qin.viewcampus.service.IUserService;
import com.qin.viewcampus.util.TransportSpecification;
import com.qin.viewcampus.util.UserProof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IUserEventService iUserEventService;

    //通过id获取用户关键信息
    @GetMapping("/{id}")
    public TransportSpecification GetUser(@PathVariable String id){
        return new TransportSpecification(true, iUserService.getById(id));
    }

    //通过id获取用户拥有的events
    @GetMapping("events/{id}/{status}")
    public TransportSpecification GetEventsOfUser(@PathVariable String id,@PathVariable Boolean status){
        return new TransportSpecification(true, iUserService.StateEventsOfUser(iUserEventService.EventsIdOfUser(id),status));
    }

    //通过id判断用户是否存在
    @GetMapping("/exist/{id}")
    public TransportSpecification UserIsExist(@PathVariable String id){
        return new TransportSpecification(iUserService.UserIsExist(id));
    }

    //添加user
    @PostMapping("/save")
    public TransportSpecification SaveUser(@RequestBody UserProof userProof){
        return new TransportSpecification(iUserService.SaveUser(userProof));
    }

    //修改user
    @PutMapping("/modify")
    public TransportSpecification UpdateUser(@RequestBody User user){
        return new TransportSpecification(iUserService.updateById(user));
    }

    //删除user
    @DeleteMapping("/delete")
    private TransportSpecification DeleteUserById(@PathVariable String id){
        return new TransportSpecification(iUserService.DeleteUserById(id));
    }
}
