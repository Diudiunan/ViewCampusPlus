package com.qin.viewcampus.util;

import java.util.concurrent.*;

//此处用来检查活动并根据活动结束时间进行状态更新
public class ConstantUpEventStatus {

    public void adasa(){
        ExecutorService pool = new ThreadPoolExecutor(3,5,
                6, TimeUnit.SECONDS,new ArrayBlockingQueue<>(5), Executors.defaultThreadFactory());
    }
}
