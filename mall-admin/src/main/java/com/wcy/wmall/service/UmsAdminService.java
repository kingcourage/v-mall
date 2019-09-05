package com.wcy.wmall.service;

import com.wcy.wmall.dto.UmsAdminParam;
import com.wcy.wmall.model.UmsAdmin;
import com.wcy.wmall.model.UmsPermission;
import com.wcy.wmall.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName UmsAdminService
 * @Description TODO
 * @Author wcy
 * @Date 2019-08-29 14:36
 * @Version 1.0
 **/
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     * @param username
     * @return
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     * @param umsAdminParam
     * @return
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     * @param username
     * @param password
     * @return
     */
    String login(String username,String password);

    /**
     *刷新token
     * @param oldToken
     * @return
     */
    String refreshToken(String oldToken);

    /**
     *根据id获取用户
     * @param id
     * @return
     */
    UmsAdmin getItem(Long id);

    /**
     *根据用户名或昵称分页查询用户
     * @param username
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsAdmin> list(String username,Integer pageSize,Integer pageNum);

    /**
     *修改指定用户信息
     * @param id
     * @param admin
     * @return
     */
    int update(Long id,UmsAdmin admin);

    /**
     * 删除指定用户
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     *修改用户角色关系
     * @param adminId
     * @param roleIds
     * @return
     */
    @Transactional
    int updateRole(Long adminId,List<Long> roleIds);

    /**
     *获取用户的角色
     * @param admin
     * @return
     */
    List<UmsRole> getRoleList(Long admin);

    /**
     *修改用户的权限
     * @param adminId
     * @param permissionIds
     * @return
     */
    int updatePermission(Long adminId,List<Long> permissionIds);

    /**
     * 获取用户的所有权限（包括角色和权限）
     * @param adminId
     * @return
     */
    List<UmsPermission> getPermissionList(Long adminId);

}
