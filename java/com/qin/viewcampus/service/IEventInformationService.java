package com.qin.viewcampus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.viewcampus.entity.EventInformation;

import java.util.ArrayList;

public interface IEventInformationService extends IService<EventInformation> {

    //用id获得一个活动的所有信息
    ArrayList<Object> GetEventAllInformation(String id);
}
