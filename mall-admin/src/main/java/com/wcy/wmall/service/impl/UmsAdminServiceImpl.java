package com.wcy.wmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.wcy.wmall.dto.UmsAdminParam;
import com.wcy.wmall.mapper.UmsAdminLoginLogMapper;
import com.wcy.wmall.mapper.UmsAdminMapper;
import com.wcy.wmall.mapper.UmsAdminRoleRelationMapper;
import com.wcy.wmall.model.*;
import com.wcy.wmall.service.UmsAdminService;
import com.wcy.wmall.util.JwtTokenUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.Encoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UmsAdminServiceImpl
 * @Description TODO
 * @Author wcy
 * @Date 2019-09-05 14:29
 * @Version 1.0
 **/
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminService.class);
    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UmsAdminRoleRelationMapper umsAdminRoleRelationMapper;


    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(adminList)){
            return adminList.get(0);
        }
        return null;
    }

    @Override
    public UmsAdmin register(UmsAdminParam umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam,umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);

        //查询是否有相同的用户名
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if(umsAdminList.size() > 0){
            return null;
        }

        //密码进行加密操作
        String encodePassword  = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        //密码需要客户端加密传输
        try{
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(passwordEncoder.matches(password,userDetails.getPassword())){
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
            insertLoginLog(username);
            updateLoginTimeByUsername(username);
        }catch (Exception e){
            LOGGER.warn("登录异常:{}",e.getMessage());
        }
        return token;
    }

    /**
     * 添加登录记录
     * @param username
     */
    private void insertLoginLog(String username){
        UmsAdmin umsAdmin = getAdminByUsername(username);
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(umsAdmin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }


    /**
     *根据用户名修改登录时间
     * @param username
     */
    private void updateLoginTimeByUsername(String username){
        UmsAdmin record = new UmsAdmin();
        record.setLoginTime(new Date());
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        adminMapper.updateByExampleSelective(record,example);
    }

    @Override
    public String refreshToken(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        if(jwtTokenUtil.canRefresh(oldToken)){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<UmsAdmin> list(String username, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%"+username+"%");
            example.or(example.createCriteria().andNickNameLike("%"+username+"%"));
        }
        return adminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        //密码修改单独处理
        admin.setPassword(null);
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public int delete(Long id) {
        return adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();
        //删除原来的关系
        UmsAdminRoleRelationExample umsAdminRoleRelationExample = new UmsAdminRoleRelationExample();
        umsAdminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        umsAdminRoleRelationMapper.deleteByExample(umsAdminRoleRelationExample);
        //建立新关系
        if(!CollectionUtils.isEmpty(roleIds)){
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for(Long roleId : roleIds){
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
        }

        return 0;
    }

    @Override
    public List<UmsRole> getRoleList(Long admin) {
        return null;
    }

    @Override
    public int updatePermission(Long adminId, List<Long> permissionIds) {
        return 0;
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return null;
    }
}
