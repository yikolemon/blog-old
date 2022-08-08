package com.yikolemon.aspect;

import com.yikolemon.exception.SearchLimitException;
import com.yikolemon.util.IpUtils;
import com.yikolemon.util.RedisKeyUtil;
import com.yikolemon.util.RedisUtil;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component

public class SearchLimitAspect {

    @Pointcut("execution(* com.yikolemon.web.SearchController.*(..))")
    public void searchLimit() {}


    @Before("searchLimit()")
    public void doBeforeSearch(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtils.getIpFromRequest(request);
        //String ip = request.getRemoteAddr();
        Jedis jedis = RedisUtil.getJedis();
        String princpal = RedisKeyUtil.getSearchLimitKey(ip);
        if (jedis.get(princpal)==null){
            //不存在,放行,设置redis过期
            Transaction multi = jedis.multi();
            multi.set(princpal,"1");
            multi.setex(princpal,10,"s");
            multi.exec();
            multi.close();
            jedis.close();
        }else {
            //存在,不能放行
            throw new SearchLimitException();
        }
    }



}
