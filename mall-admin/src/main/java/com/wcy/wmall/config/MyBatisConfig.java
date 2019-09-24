package com.wcy.wmall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MyBatisConfig
 * @Description TODO
 * @Author wcy
 * @Date 2019-09-10 11:50
 * @Version 1.0
 **/
@Configuration
@EnableAutoConfiguration
@MapperScan({"com.wcy.wmall.mapper","com.wcy.wmall.dao"})
public class MyBatisConfig {
}
