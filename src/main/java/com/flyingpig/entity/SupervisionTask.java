package com.flyingpig.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("supervision_task")
public class SupervisionTask {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userid;
    private Integer courseId;
}
