package com.boyoi.core.config;

import com.boyoi.core.multi.MultiRequestBodyArgumentResolver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * spring mvc 配置
 *
 * @author ZhouJL
 * @date 2018/12/25 11:26
 */
@EnableAutoConfiguration
@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.servlet.multipart.maxFileSize}")
    private String MaxFileSize;
    @Value("${spring.servlet.multipart.maxRequestSize}")
    private String MaxRequestSize;
    @Value("${allowedOrigin}")
    private String allowedOrigin;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 注册ConverterFactory(类型转换器工厂)
        registry.addConverterFactory(new EnumConvertFactory());
    }

    /**
     * 增加映射配置文件，/ -> index.html
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //logger.info("xxx:addViewControllers 增加地址映射");
        registry.addViewController("/").setViewName("/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //静态文件路径设置
        registry.addResourceHandler("/**").addResourceLocations("classpath:/dist/");
        registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/dist/static/img/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // 添加MultiRequestBody参数解析器
        argumentResolvers.add(new MultiRequestBodyArgumentResolver());
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        // 解决中文乱码问题
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        WebMvcConfigurer.super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    /**
     * 解决jar包运行上传文件找不到tmp问题
     *
     * @return MultipartConfigElement
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(System.getProperty("java.io.tmpdir"));
        //  单个数据大小
        factory.setMaxFileSize(DataSize.parse(MaxFileSize));
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.parse(MaxRequestSize));
        return factory.createMultipartConfig();
    }

    /**
     * CORS
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(allowedOrigin.split(",")));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
