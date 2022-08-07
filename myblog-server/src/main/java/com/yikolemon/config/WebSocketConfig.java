package com.yikolemon.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{

    /** 扫描注解了@ServerEndpoint注解的类 */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();  
    }


    /*修改握手前数据*/
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        //把username放入ServerEndpointConfig
        try {
            sec.getUserProperties().put("username", SecurityUtils.getSubject().getPrincipal());
        } catch (NullPointerException e) {
            //身份校验错误
            return;
        }
        super.modifyHandshake(sec, request, response);
    }

}
