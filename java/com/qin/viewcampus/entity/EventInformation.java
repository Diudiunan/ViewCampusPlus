package com.qin.viewcampus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("event_informations")
public class EventInformation {
    @TableId("event_id")
    private String eventId;
    private String eventSite;
    private String eventContent;
    private String eventStart;
    private String eventEnd;
}
