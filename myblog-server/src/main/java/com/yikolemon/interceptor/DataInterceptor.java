package com.yikolemon.interceptor;

import com.yikolemon.service.DataServiceImpl;
import com.yikolemon.service.UserService;
import com.yikolemon.util.IpUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataInterceptor implements HandlerInterceptor {

    @Resource
    private DataServiceImpl dataService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //统计uv
        String ip = IpUtils.getIpFromRequest(request);
        //String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        //统计Dau
        Subject subject = SecurityUtils.getSubject();
        boolean authenticated = subject.isAuthenticated();
        //如果登录了
        if (authenticated){
            Long id = userService.getUserByUsername((String) subject.getPrincipal()).getId();
            dataService.recordDAU(id.intValue());
        }
        return true;
    }

}
