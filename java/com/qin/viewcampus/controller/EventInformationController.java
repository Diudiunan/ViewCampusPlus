package com.qin.viewcampus.controller;

import com.qin.viewcampus.entity.EventInformation;
import com.qin.viewcampus.service.IEventInformationService;
import com.qin.viewcampus.util.TransportSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventinformation")
public class EventInformationController {

    @Autowired
    private IEventInformationService iEventInformationService;

    // EventController里也有此方法
    //用于用户个人根据id查看event详细信息
    @GetMapping("/{id}")
    public TransportSpecification GetDetailedEventById(@PathVariable String id){
        return new TransportSpecification(true,iEventInformationService.GetEventAllInformation(id));
    }

    //添加event的信息
    @PostMapping
    public TransportSpecification SaveEventI(@RequestBody EventInformation eventInformation){
        return new TransportSpecification(iEventInformationService.save(eventInformation));
    }

    //修改event的信息
    @PutMapping
    public TransportSpecification UpdateEvent(@RequestBody EventInformation eventInformation){
        return new TransportSpecification(iEventInformationService.updateById(eventInformation));
    }

    /* 将此功能归到EventController的DeletEventById中
    //删除event信息
    @DeleteMapping
    private TransportSpecification DeletEventIById(@PathVariable String id){
        return new TransportSpecification(iEventInformationService.removeById(id));
    }
     */
}
