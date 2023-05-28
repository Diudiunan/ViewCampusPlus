package com.qin.viewcampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.viewcampus.entity.User;
import com.qin.viewcampus.util.UserProof;

import java.util.ArrayList;
import java.util.List;

public interface IUserService extends IService<User> {

    //通过id删除用户
    Boolean DeleteUserById(String id);
    //创建新用户
    Boolean SaveUser(UserProof userProof);
    //通过id查询用户是否存在
    Boolean UserIsExist(String id);
    //以活动id集和激活态为依据获取用户的event的全部信息
    List<ArrayList<Object>> StateEventsOfUser(List<String> idList, Boolean status);
}
