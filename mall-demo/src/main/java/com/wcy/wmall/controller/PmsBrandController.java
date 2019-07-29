package com.wcy.wmall.controller;

import com.wcy.wmall.service.DemoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PmsBrandController
 * @Description TODO
 * @Author wcy
 * @Date 2019-07-29 16:19
 * @Version 1.0
 **/
@Api(value = "demo",description = "demo详情")
@Controller
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private DemoService demoService;


}
