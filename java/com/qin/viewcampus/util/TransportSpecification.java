package com.qin.viewcampus.util;

import lombok.Data;

//用于向前端传送统一规范的数据
@Data
public class TransportSpecification {
    private Boolean res;
    private Object data;

    public TransportSpecification(){};
    public TransportSpecification(Boolean res){
        this.res = res;
    }
    public TransportSpecification(Boolean res, Object data){
        this.res = res;
        this.data = data;
    }
}
