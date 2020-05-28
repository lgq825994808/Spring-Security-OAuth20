package com.adb.security.springmvc.mode;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String userName;
    private String password;

    /**
     * 用户权限
     */
    private Set<String> authorities;
}
