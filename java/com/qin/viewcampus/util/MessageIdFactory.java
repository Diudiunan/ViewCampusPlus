package com.qin.viewcampus.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class MessageIdFactory {

    //获得当前的使用的表标号
    public String GetTableLabel(){
        return String.valueOf(LocalDate.now()).replace("-","").substring(5,8);
    }

    //获得系统消息的id
    public String GetMID(){
        return String.valueOf(LocalTime.now()).replace(":","").replace(".","");
    }
}
