package com.wcy.wmall.component;

import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.wcy.wmall.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName RestfulAccessDeniedHandler
 * @Description 当接口没有访问权限的时候，自定义的返回结果
 * @Author wcy
 * @Date 2019-09-25 11:27
 * @Version 1.0
 **/
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(CommonResult.forbidden(e.getMessage())));
        response.getWriter().flush();
    }
}
