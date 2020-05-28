package com.adb.security.springmvc.service.impl;

import com.adb.security.springmvc.mode.UserDto;
import com.adb.security.springmvc.mode.AuthenticationRequest;
import com.adb.security.springmvc.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private Map<String,UserDto> dataMap=new HashMap<String,UserDto>();
    {
        Set<String> authorities1 = new HashSet<>();
        authorities1.add("p1");//这个p1我们人为让它和/r/r1对应
        Set<String> authorities2 = new HashSet<>();
        authorities2.add("p2");//这个p2我们人为让它和/r/r2对应

        dataMap.put("zhangsan",new UserDto(1,"zhangsan","123456",authorities1));
        dataMap.put("lisi",new UserDto(2,"lisi","111222",authorities2));
    }

    @Override
    public UserDto authentication(AuthenticationRequest userRequest) {
        if(userRequest==null
                || StringUtils.isEmpty(userRequest.getUsername())
                || StringUtils.isEmpty(userRequest.getPassword())){
            throw new RuntimeException("用户名或密码不能为空");
        }
        UserDto user = getUser(userRequest.getUsername());
        if(user==null){
            throw new RuntimeException("用户未注册");
        }
        if(!user.getPassword().equals(userRequest.getPassword())){
            throw new RuntimeException("密码不正确");
        }
        return user;
    }

    public UserDto getUser(String userName){
       return dataMap.get(userName);
    }

}
