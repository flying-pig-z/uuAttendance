package com.flyingpig.dataobject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordVO {
    private String no;
    private String oldPassword;
    private String newPassword;

}
