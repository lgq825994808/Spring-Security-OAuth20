package com.adb.security.distributed.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // clients.withClientDetails(clientDetailsService);
        clients.inMemory()// 使用in‐memory存储
                 .withClient("c1")// client_id
                .secret(new BCryptPasswordEncoder().encode("secret")) //客户端秘钥
                .resourceIds("res1") //资源列表
                .authorizedGrantTypes("authorization_code", "password","client_credentials","implicit","refresh_token")// 该client允许的授权类型 authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                 .autoApprove(false) //加上验证回调地址
                .redirectUris("http://www.baidu.com");
    }

    //令牌存储策略
    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//客户端信息服务
        service.setSupportRefreshToken(true);//示范产生刷新令牌
        service.setTokenStore(tokenStore);//立牌存储策略
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }


    //令牌访问端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints .authenticationManager(authenticationManager)//密码模式需要
                .authorizationCodeServices(authorizationCodeServices)//授权码模式需要
                .tokenServices(tokenService())//令牌管理服务
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);//允许post提交
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        //设置授权码模式的授权码如何 存取，暂时采用内存方式
        return new InMemoryAuthorizationCodeServices();
    }

    //令牌访问端点安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security .tokenKeyAccess("permitAll()") //oauth/token_key 公开
                .checkTokenAccess("permitAll()") ///oauth/check_token 公开
                .allowFormAuthenticationForClients();//表单认证，申请令牌
    }
}
