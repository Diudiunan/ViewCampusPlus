package com.qin.viewcampus.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin")
public class Admin {
    @TableId("admin_id")
    private String adminId;
    private String adminPass;
}
