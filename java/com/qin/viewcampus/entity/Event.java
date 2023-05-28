package com.qin.viewcampus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("events")
public class Event {
    @TableId("event_id")
    private String eventId;
    private String userId;
    private String eventName;
    private String eventSponsor;
    private Integer eventGrade;
    private Integer statusApproval;
    private Integer statusEnd;
}
