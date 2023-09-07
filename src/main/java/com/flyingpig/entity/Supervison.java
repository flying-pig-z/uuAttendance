package com.flyingpig.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("supervision")
public class Supervison {
    private Integer id;
    private String no;
    private Integer StudentId;
}