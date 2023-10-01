package com.flyingpig.service;

import com.flyingpig.common.Result;
import com.flyingpig.dataobject.entity.User;

public interface LoginService {
    Result login(User user);
    Result logout();

    Integer getAuthenticateByUserId(String userId);

    void updateUserWithPassword(String no,String newPassword);

    void addUser(User user);

    String getPasswordByNo(String no);
}
