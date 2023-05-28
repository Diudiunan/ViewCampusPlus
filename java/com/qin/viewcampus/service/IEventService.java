package com.qin.viewcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qin.viewcampus.entity.Event;
import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.util.EventsToMarkers;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public interface IEventService extends IService<Event> {

    //获得所有已激活的event
    List<Event> GetActiveEvents();
    //获得所有失活的event
    List<Event> GetOffEvents();
    //获得被推荐的已激活的活动
    List<Event> GetRecommendActiveEvents(int currentpage, int pagesize, int recommend);
    //获得所有已激活的event并带有坐标
    List<EventsToMarkers> GetActiveMarkers(Integer filter);
    //按要求获得分页，包括激活态的和失效态的活动
    IPage<Event> GetPage(int currentpage, int pagesize);
    //用id获得一个活动的所有信息
    ArrayList<Object> GetEventAllInformation(String id);
    //搜索id并返回此活动的所有信息
    ArrayList<Object> GetSearchResultById(String id);
    //通过id删除活动
    Boolean DeleteEventById(String id);
    //保存活动及信息
    Boolean SaveEvent(Event event, EventInformation eventInformation, String uid);
    //异步更新活动状态
    void UpdateEvent();
}
