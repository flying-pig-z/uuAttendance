package com.flyingpig.service;

import com.flyingpig.pojo.Result;
import com.flyingpig.dataobject.entity.User;

public interface LoginService {
    Result login(User user);
    Result logout();

    Integer getAuthenticateByUserId(String userId);

    void updateUserWithPassword(String userId,String newPassword);
}
