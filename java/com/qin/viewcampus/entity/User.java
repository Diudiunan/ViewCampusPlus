package com.qin.viewcampus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class User {
    @TableId("user_id")
    private String userId;
    private String userAccount;
    private Integer userPass;
    private Integer userRight;
}
