package com.yikolemon.interceptor;


        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
//    public DataInterceptor dataInterceptor(){
//        return new DataInterceptor();
//    }
//
//    @Resource
//    private DataInterceptor dataInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(dataInterceptor)
//                .addPathPatterns("/");
//    }

}
