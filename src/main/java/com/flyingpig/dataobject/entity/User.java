package com.flyingpig.dataobject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String no;
    private String password;
    private String name;
    private String gender;
    private String college;
    private Integer userType;
}
