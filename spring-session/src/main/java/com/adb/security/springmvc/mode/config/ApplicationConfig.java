package com.adb.security.springmvc.mode.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
 * 该配置类相当于applicationContext.xml
 */
@Configuration
//扫描包路径，并排除Controller
@ComponentScan(basePackages = "com.adb.security.springmvc",
                excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = Controller.class)})
public class ApplicationConfig {
    //在此处配置除了Controller的其他bean,比如：数据库连接池，事物管理器，业务bean等


}
