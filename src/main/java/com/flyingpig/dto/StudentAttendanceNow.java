package com.flyingpig.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceNow {
    private Integer courseId;
    private Integer status;
    private String courseName;
    private String longitude;
    private String latitude;
    //重写比较方法
    @Override
    public boolean equals(Object obj) {
        // 检查是否引用同一对象
        if (this == obj) {
            return true;
        }
        // 检查对象是否为null或者类型不匹配
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        // 将obj强制转换为当前类的类型
        StudentAttendanceNow other = (StudentAttendanceNow) obj;
        // 比较courseId和status属性是否相等
        if (!Objects.equals(this.courseId, other.courseId)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        // 其他属性不需要比较

        // 所有属性都相等，返回true
        return true;
    }
}
