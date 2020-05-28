package com.adb.security.springmvc.service;

import com.adb.security.springmvc.mode.UserDto;
import com.adb.security.springmvc.mode.AuthenticationRequest;

public interface AuthenticationService {
    /**
     * 登录方法
     * @param userRequest
     * @return
     */
    UserDto authentication(AuthenticationRequest userRequest);
}
