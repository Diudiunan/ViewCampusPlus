package com.qin.viewcampus.util;

import com.qin.viewcampus.entity.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventRecommend {

    private List<Event> list;
    private Integer mode;

    public EventRecommend(List<Event> list, Integer mode) {
        this.list = list;
        this.mode = mode;
    }

    private List<Event> ModeDefault(){
        return list;
    }

    private List<Event> ModeNearTime(){
        Collections.reverse(list);
        return list;
    }

    public List<Event> GetRecommendList(){
        switch (mode){
            case 0:
                return ModeDefault();
            case 1:
                return ModeNearTime();
            default:
                return list;
        }
    }
}
