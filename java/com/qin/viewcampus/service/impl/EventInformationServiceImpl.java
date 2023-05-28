package com.qin.viewcampus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.viewcampus.dao.EventDao;
import com.qin.viewcampus.dao.EventInformationDao;
import com.qin.viewcampus.entity.Event;
import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.service.IEventInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EventInformationServiceImpl extends ServiceImpl<EventInformationDao, EventInformation> implements IEventInformationService {

    @Autowired
    private EventInformationDao eventInformationDao;
    @Autowired
    private EventDao eventDao;
    //通过id获得event的所有信息
    @Override
    public ArrayList<Object> GetEventAllInformation(String id) {
        ArrayList<Object> eventList= new ArrayList<Object>();
        Event event = eventDao.selectById(id);
        EventInformation eventInformation = eventInformationDao.selectById(id);
        eventList.add(event);
        eventList.add(eventInformation);
        return eventList;
    }
}
