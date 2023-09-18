package com.flyingpig.dataobject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRegisterVO {
    public String email;
    public String verificationCode;
    //工号
    private String no;
    //密码
    public String password;

}
