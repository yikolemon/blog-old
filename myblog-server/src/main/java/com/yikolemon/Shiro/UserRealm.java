package com.yikolemon.Shiro;

import com.yikolemon.pojo.User;
import com.yikolemon.service.UserService;
import com.yikolemon.util.ApplicationContextUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserRealm extends AuthorizingRealm {

    public UserRealm() {
        //修改凭证校验匹配器
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //设置算法为md5
        matcher.setHashAlgorithmName("MD5");
        //设置散列次数
        matcher.setHashIterations(1024);
        this.setCredentialsMatcher(matcher);
        this.setCacheManager(new RedisCacheManager());
        this.setCachingEnabled(true);
        this.setAuthenticationCacheName("AuthenticationCache");
        this.setAuthorizationCacheName("AuthorizationCache");
        this.setAuthenticationCachingEnabled(true);//认证缓存
        this.setAuthorizationCachingEnabled(true);//授权缓存
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");
        Boolean admin = userService.isAdmin((String) principalCollection.getPrimaryPrincipal());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (admin){
            info.addRole("admin");
        }else {
            info.addRole("user");
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String user_name = (String) authenticationToken.getPrincipal();
        UserService userService = (UserService) ApplicationContextUtils.getBean("userServiceImpl");
        User user = userService.getUserByUsername(user_name);
        String salt = userService.getSaltByUsername(user_name);
        MySimpleByteSource mySimpleByteSource = new MySimpleByteSource(salt);
        if (user==null){
            return null;
        }else {
            SimpleAuthenticationInfo simpleAuthenticationInfo =
                    new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(),mySimpleByteSource,this.getName());
            return simpleAuthenticationInfo;
        }
    }
}
