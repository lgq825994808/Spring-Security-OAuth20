package com.adb.security.distributed.uaa.service;


import com.adb.security.distributed.uaa.dao.UserDao;
import com.adb.security.distributed.uaa.model.UserDto;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @version 1.0
 * security 验证用户名和密码的类，需要自己实现，
 * 只需实现UserDetailsService接口，实现里面的loadUserByUsername方法即可
 **/
@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //将来连接数据库根据账号查询用户信息
        UserDto userDto = userDao.getUserByUsername(username);
        if(userDto == null){
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }
        //根据用户的id查询用户的权限
        List<String> permissions = userDao.findPermissionsByUserId(userDto.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //这里将user转为json，将整体user存入userDetails
        //将用户的所有信息转成存入userName属性，需要用户信息时，在转成对象（属于偷懒行为）
        String principal =JSON.toJSONString(userDto);
        UserDetails userDetails = User.withUsername(principal).password(userDto.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
