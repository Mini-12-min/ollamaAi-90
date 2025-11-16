package com.cuit.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许SpringMVC的所有路径的跨域请求
                .allowedOrigins("http://localhost:5500","http://127.0.0.1:5500") //允许的所有的源(域名)
                .allowedMethods("*") //允许所有的HTTP方法
                .allowedHeaders("*") //允许所有头部信息
                .allowCredentials(true) // 是否允许发送Cookie
                .maxAge(60*60); //预检请求的缓存时间（秒）
    }
}
