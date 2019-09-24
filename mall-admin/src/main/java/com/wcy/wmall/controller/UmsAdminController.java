package com.wcy.wmall.controller;
import	java.lang.reflect.Parameter;

import com.wcy.wmall.common.api.CommonPage;
import com.wcy.wmall.common.api.CommonResult;
import com.wcy.wmall.dto.UmsAdminParam;
import com.wcy.wmall.model.UmsAdmin;
import com.wcy.wmall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UmsAdminController
 * @Description TODO
 * @Author wcy
 * @Date 2019-08-29 14:34
 * @Version 1.0
 **/
@Controller
@Api(tags = "UmsAdminController",description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    @Autowired
    private UmsAdminService adminService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt,tokenHead}")
    private String tokenHead;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdminParam umsAdminParam, BindingResult result){
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if(umsAdmin == null){
            CommonResult.failed();
        }
        return CommonResult.success(umsAdmin);
    }

    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody UmsAdminParam umsAdminParam, BindingResult result){
        String token = adminService.login(umsAdminParam.getUsername(),umsAdminParam.getPassword());
        if(token == null){
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "刷新token")
    @RequestMapping(value = "/token/refresh",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult refreshToken(HttpServletRequest request){
        String token =request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if(refreshToken == null){
            return CommonResult.failed();
        }
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAdminInfo(Principal principal){
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        Map<String,Object> data = new HashMap<>();
        data.put("username",umsAdmin.getUsername());
        data.put("roles",new String[]{"TEST"});
        data.put("icon",umsAdmin.getIcon());
        return CommonResult.success(data);
    }

    @ApiOperation(value ="登出功能")
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout(){
        return CommonResult.success(null);
    }


    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "name",required = false)String name,
                                                   @RequestParam(value = "pageSize",defaultValue ="5")Integer pageSize,
                                                   @RequestParam(value = "pageNum",defaultValue ="1")Integer pageNum){
        List<UmsAdmin>adminList = adminService.list(name,pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }


    @ApiOperation("修改指定用户的信息")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id,@RequestBody UmsAdmin umsAdmin){
        int count = adminService.update(id,umsAdmin);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("删除指定用户信息")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id){
        int count = adminService.delete(id);
        if(count > 0 ){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }


    @ApiOperation("给用户分配角色")
    @RequestMapping(value = "/role/update",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("adminId")Long adminId,
                                   @RequestParam("roleId")List<Long> roleIds){
        int count = adminService.updateRole(adminId, roleIds);
        if(count >= 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }




}
