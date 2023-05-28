package com.qin.viewcampus.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Callable;

//活动id工厂
public class EventIdFactory implements Callable<String> {

    //生产id
    @Override
    public  String call() throws Exception {
        String theDate = String.valueOf(LocalDate.now()).replace("-","");
        String theTime = String.valueOf(LocalTime.now()).replace(":","");
        String uid = theDate.substring(2,7) + theTime.replace(".","");
        return uid;
    }
}
