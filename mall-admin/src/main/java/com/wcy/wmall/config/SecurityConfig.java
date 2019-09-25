package com.wcy.wmall.config;

import com.wcy.wmall.component.RestAuthenticationEntryPoint;
import com.wcy.wmall.component.RestfulAccessDeniedHandler;
import com.wcy.wmall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author wcy
 * @Date 2019-09-25 11:10
 * @Version 1.0
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,//允许对网站静态资源的无权限访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers("/admin/login","admin/register")
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers("/**") //测试时全部允许访问
                .permitAll()
                .anyRequest() //除上面的都需要鉴权认证
                .authenticated();
        //禁用缓存
        httpSecurity.headers().cacheControl();
        //添加JWT filter
    }
}
