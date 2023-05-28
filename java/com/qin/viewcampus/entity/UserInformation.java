package com.qin.viewcampus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_informations")
public class UserInformation {
    @TableId("user_id")
    private String userId;
    private String userName;
    private String userIntroduction;
    private String userUnit;
}
