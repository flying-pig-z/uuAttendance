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
@TableName("student")
public class Student {

    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;
    private String no;
    private String name;
    private String grade;
    private String clasS;
    private String major;
    private Integer userid;
}
