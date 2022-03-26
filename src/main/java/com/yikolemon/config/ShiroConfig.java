package com.yikolemon.config;

import com.yikolemon.Shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/*用来配置shiro*/
@Configuration
public class ShiroConfig {

    //开始shiro注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
                = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //1.创建shiroFilter
    //负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager manager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(manager);
        //配置系统受限资源
        //配置系统公共资源

        HashMap<String, String> map = new HashMap<>();
        //让认证相关请求通过，anon为公共资源，需要放在/**前面，注意路径为/user/login
        map.put("/**","anon");
        map.put("/admin/**","authc");
        map.put("/admin","anon");
        map.put("/admin/login","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        //设置默认认证界面路径
        shiroFilterFactoryBean.setLoginUrl("/admin");
        shiroFilterFactoryBean.setSuccessUrl("admin/index");
        return shiroFilterFactoryBean;
    }
    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //给安全管理器设置realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }


    //创建自定义realm
    @Bean
    public Realm getRealm(){
        return new UserRealm();
    }


}