package com.wcy.wmall.dao;

import com.wcy.wmall.model.UmsAdminRoleRelation;
import com.wcy.wmall.model.UmsPermission;
import com.wcy.wmall.model.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsAdminRoleRelationDao {

    /**
     *批量插入用户角色关系
     * @param adminRoleRelationList
     * @return
     */
    int insertList(@Param("list")List<UmsAdminRoleRelation> adminRoleRelationList);

    /**
     *获取用户所有角色
     * @param adminId
     * @return
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);

    /**
     *获取用户所有角色权限
     * @param adminId
     * @return
     */
    List<UmsPermission> getRolePermissionList(@Param("adminId") Long adminId);

    /**
     *获取用户所有权限（）
     * @param adminId
     * @return
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);

}
