package com.yikolemon.aspect;

import com.yikolemon.exception.SearchLimitException;
import com.yikolemon.util.IpUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class SearchLimitAspect {

    // 使用 ConcurrentHashMap 存储每个 IP 的请求计数和时间戳
    private static final ConcurrentHashMap<String, RequestInfo> requestMap = new ConcurrentHashMap<>();
    private static final int LIMIT = 1; // 限制每个 IP 每次操作的次数
    private static final long EXPIRY_TIME = 3 * 1000; // 过期时间，单位毫秒
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 初始化清理过期 IP 的定时任务
    static {
        scheduler.scheduleAtFixedRate(SearchLimitAspect::cleanExpiredIps, 1, 5, TimeUnit.SECONDS);
    }

    @Pointcut("execution(* com.yikolemon.web.SearchController.*(..))")
    public void searchLimit() {}

    @Before("searchLimit()")
    public void doBeforeSearch() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = IpUtils.getIpFromRequest(request);

        // 获取当前请求的记录
        RequestInfo requestInfo = requestMap.get(ip);

        if (requestInfo == null) {
            // 如果没有记录，表示是第一次访问
            requestMap.put(ip, new RequestInfo(1, System.currentTimeMillis()));
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - requestInfo.timestamp < EXPIRY_TIME) {
                // 请求次数未过期
                if (requestInfo.count >= LIMIT) {
                    // 如果请求次数达到限制，抛出异常
                    throw new SearchLimitException();
                } else {
                    // 增加请求次数
                    requestInfo.count++;
                    requestInfo.timestamp = currentTime; // 更新时间戳
                }
            } else {
                // 如果请求过期，重新设置请求计数
                requestInfo.count = 1;
                requestInfo.timestamp = currentTime;
            }
        }
    }

    // 清理过期的 IP 记录
    private static void cleanExpiredIps() {
        long currentTime = System.currentTimeMillis();
        requestMap.entrySet().removeIf(entry -> currentTime - entry.getValue().timestamp >= EXPIRY_TIME);
    }

    // 内部类，用于存储请求的计数和时间戳
    private static class RequestInfo {
        int count; // 请求计数
        long timestamp; // 时间戳，表示上次请求的时间

        public RequestInfo(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}
