package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteCareersService;
import com.ys.system.api.RemoteEmployeesService;
import com.ys.system.api.domain.HrEmployees;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.mapper.SysEnterpriseMapper;
import com.ys.system.mapper.SysRoleMenuMapper;
import com.ys.system.mapper.SysUserMapper;
import com.ys.system.service.ISysEnterpriseService;
import com.ys.system.service.ISysRoleService;
import com.ys.system.service.ISysUserService;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Enterprise management Service Business Layer Processing
 *
 * @author ys
 * @date 2025-05-15
 */
@Service
public class SysEnterpriseServiceImpl extends ServiceImpl<SysEnterpriseMapper, SysEnterprise> implements ISysEnterpriseService
{
    @Resource
    private ISysRoleService roleService;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Resource
    private RemoteEmployeesService employeesService;

    @Resource
    private RemoteCareersService remoteCareersService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private EmailUtils emailUtils;
    /**
     * Query Enterprise management   list
     *
     *
     * @param sysEnterprise  Enterprise management
     * @return  Enterprise management
     */
    @Override
    public List<SysEnterprise> selectSysEnterpriseList(SysEnterprise sysEnterprise)
    {
        return baseMapper.selectSysEnterpriseList(sysEnterprise);
    }

    @Override
    public SysEnterprise selectSysEnterpriseByInviteCode(String code){
        return baseMapper.selectSysEnterpriseByInviteCode(code);
    }

    @Override
    @Transactional
    public int insertSysEnterprise(SysEnterprise sysEnterprise) {
        sysEnterprise.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(sysEnterprise);
        String enterpriseId = sysEnterprise.getId();
        HrEmployees hrEmployees = new HrEmployees();
        hrEmployees.setAvatarUrl(sysEnterprise.getLogoUrl());
        hrEmployees.setEnterpriseId(enterpriseId);
        String username = sysEnterprise.getUsername();
        hrEmployees.setUsername(username);
        String password = sysEnterprise.getPassword();
        hrEmployees.setPassword(password);
        hrEmployees.setPhoneCode(sysEnterprise.getPhoneCode());
        hrEmployees.setContactPhone(sysEnterprise.getContactPhone());
        hrEmployees.setFullName(sysEnterprise.getFullName());
        hrEmployees.setEmail(sysEnterprise.getContactEmail());
        hrEmployees.setJobnumber(sysEnterprise.getJobnumber());
        SysRole role = new SysRole();
        role.setType("2");
        List<SysRole> sysRoles = roleService.selectRoleList(role);
        for (SysRole sysRole : sysRoles) {
            sysRole.setMenuIds(roleMenuMapper.selectMenuByRoleId(sysRole.getRoleId()));
            sysRole.setRoleId(null);
            sysRole.setType("3");
            sysRole.setEnterpriseId(enterpriseId);
            roleService.insertRole(sysRole);
        }
        role.setEnterpriseId(enterpriseId);
        role.setRoleKey("admin");
        role.setType("3");
        List<SysRole> sysRoles1 = roleService.selectRoleList(role);
        SysRole role1 = sysRoles1.get(0);
        hrEmployees.setAccessLevel(String.valueOf(role1.getRoleId()));
        hrEmployees.setUserType("01");
        AjaxResult add = employeesService.add(hrEmployees, SecurityConstants.INNER);
        remoteCareersService.add(sysEnterprise,SecurityConstants.INNER);
        if (add.isError()) {
            throw  new ServiceException(add.get("msg").toString());
        }
        if(insert>0){
            if(ObjectUtils.isNotEmpty(sysEnterprise.getContactEmail())){
                Map<String, Object> map = new HashMap<>();
                map.put("HrisToolName","Shiftcare HR");
                map.put("CompanyName",sysEnterprise.getEnterpriseName());
                map.put("LoginUrl",sysEnterprise.getLoginUrl());
                map.put("UserName",username);
                map.put("PassWord",password);
                emailUtils.sendEmailByTemplate(map, sysEnterprise.getContactEmail(), "NewCompany");
            }
            application(sysEnterprise);
        }
        return insert;
    }

    private void application(SysEnterprise sysEnterprise) {
        if(ObjectUtils.isNotEmpty(sysEnterprise.getApplicationId()) && ObjectUtils.isNotEmpty(sysEnterprise.getId())){
            baseMapper.updataByApplicationId(sysEnterprise);
        }
    }


    @Override
    @Transactional
    public int updateSysEnterprise(SysEnterprise sysEnterprise) {
        String enterpriseId = sysEnterprise.getId();
        SysEnterprise sysEnterprise1 = baseMapper.selectSysEnterpriseById(enterpriseId);
        String enterpriseName = sysEnterprise.getEnterpriseName();
        String enterpriseName1 = sysEnterprise1.getEnterpriseName();
        boolean a = enterpriseName != null && !enterpriseName1.equals(enterpriseName);
        String contactEmail = sysEnterprise.getContactEmail();
        String contactEmail1 = sysEnterprise1.getContactEmail();
        boolean b = contactEmail != null && !contactEmail1.equals(contactEmail);
        if (a||b){
            SysUser user = new SysUser();
            user.setEnterpriseId(enterpriseId);
            user.setUserType("01");
            List<SysUser> sysUsers = userService.selectUserList(user);
            if (!sysUsers.isEmpty()) {
                SysUser sysUser = sysUsers.get(0);
                SysUser sysUser1 = new SysUser();
                sysUser1.setUserId(sysUser.getUserId());
                if (a){
                    sysUser1.setNickName(enterpriseName);
                }
                SysUser sysUser2 = new SysUser();
                sysUser2.setEmail(contactEmail);
                if (b&&userService.checkEmailUnique(sysUser2)){
                    sysUser1.setEmail(contactEmail);
                }
                sysUserMapper.updateUser(sysUser1);
            }
        }
        return baseMapper.updateById(sysEnterprise);
    }

    @Override
    @Transactional
    public int updateSysEnterpriseByFirst(SysEnterprise sysEnterprise) {
        sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        int i = baseMapper.updateById(sysEnterprise);
        if(i>0){
            SysUser sysUser = new SysUser();
            sysUser.setUserId(SecurityUtils.getUserId());
            sysUser.setIsFirst('1');
            int i1 = sysUserMapper.updateUser(sysUser);
            if(i1>0){
                LoginUser loginUser1 = SecurityUtils.getLoginUser();
                SysUser sysUser1 = loginUser1.getSysUser();
                sysUser1.setIsFirst('1');
                loginUser1.setSysUser(sysUser1);
                tokenService.refreshToken(loginUser1);
            }
        }
        return i;
    }

    @Override
    public SysEnterprise selectSysEnterpriseById(String id) {
        return baseMapper.selectSysEnterpriseById(id);
    }

    @Override
    public boolean disablesEnterprise(String id) {
        SysEnterprise sysEnterprise = new SysEnterprise();
        sysEnterprise.setId(id);
        sysEnterprise.setStatus("0");
        return updateById(sysEnterprise);
    }

    @Override
    public boolean resumeEnterprise(String id) {
        SysEnterprise sysEnterprise = new SysEnterprise();
        sysEnterprise.setId(id);
        sysEnterprise.setStatus("1");
        return updateById(sysEnterprise);
    }

    @Override
    public List<SysEnterprise> selectEnterpriseList(SysEnterprise sysEnterprise) {
        return baseMapper.selectEnterpriseList(sysEnterprise);
    }

}
