package com.wcy.wmall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MyBatisConfig
 * @Description TODO
 * @Author wcy
 * @Date 2019-07-29 16:57
 * @Version 1.0
 **/
@Configuration
@MapperScan("com.wcy.wmall.mapper")
public class MyBatisConfig {
}
