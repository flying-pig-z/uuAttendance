package com.flyingpig.service;

import com.flyingpig.pojo.Result;
import com.flyingpig.entity.User;

public interface LoginService {
    Result login(User user);
    Result logout();

    Integer getAuthenticateByUserId(String userId);
}
