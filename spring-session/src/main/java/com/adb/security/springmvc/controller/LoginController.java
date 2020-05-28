package com.adb.security.springmvc.controller;


import com.adb.security.springmvc.mode.AuthenticationRequest;
import com.adb.security.springmvc.mode.UserDto;
import com.adb.security.springmvc.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class LoginController {

    @Autowired
    AuthenticationService authenticationService;

    public static final String SESSION_USER_KEY = "_user";


    @RequestMapping(value = "/login",produces = "text/plain;charset=utf-8")
    public String login(AuthenticationRequest authenticationRequest, HttpSession session){
        UserDto userDto = authenticationService.authentication(authenticationRequest);
        //存入session
        session.setAttribute(SESSION_USER_KEY,userDto);
        return userDto.getUserName()+"登录成功";
    }

    @GetMapping(value = "/logout",produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession session){
        session.invalidate();
        return "退出成功";
    }

    @GetMapping(value = "/r/r1",produces = {"text/plain;charset=UTF-8"})
    public String r1(HttpSession session){
        String fullname = null;
        Object object = session.getAttribute(SESSION_USER_KEY);
        if(object == null){
            fullname = "请登录";
        }else{
            UserDto userDto = (UserDto) object;
            fullname = userDto.getUserName();
        }
        return fullname+"访问资源r1";
    }
    @GetMapping(value = "/r/r2",produces = {"text/plain;charset=UTF-8"})
    public String r2(HttpSession session){
        String fullname = null;
        Object userObj = session.getAttribute(SESSION_USER_KEY);
        if(userObj != null){
            fullname = ((UserDto)userObj).getUserName();
        }else{
            fullname = "请登录";
        }
        return fullname + " 访问资源2";
    }
}
