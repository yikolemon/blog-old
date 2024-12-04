package com.yikolemon.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikolemon.exception.SearchLimitException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView AuthorizationHandler(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/114");
        return mv;
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e);

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ModelAndView toLogin(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("login");
        return mv;
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ModelAndView failToSend(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/500");
        return mv;
    }

    @ExceptionHandler(SearchLimitException.class)
    public ModelAndView searchLimit(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/403");
        return mv;
    }

}
