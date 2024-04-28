package com.flyingpig.dataobject.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInMessage implements Serializable {
    Integer studentId;
    Integer courseId;
}
