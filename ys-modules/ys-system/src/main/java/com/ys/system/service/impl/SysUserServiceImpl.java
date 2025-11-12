package com.ys.system.service.impl;

import com.ys.common.core.constant.UserConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.SpringUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.bean.BeanValidators;
import com.ys.common.datascope.annotation.DataScope;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysUserRole;
import com.ys.system.mapper.SysRoleMapper;
import com.ys.system.mapper.SysUserMapper;
import com.ys.system.mapper.SysUserRoleMapper;
import com.ys.system.service.ISysConfigService;
import com.ys.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * USER Business Layer Processing
 *
 * @author ruoyi
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    protected Validator validator;

    /**
     *Query USER LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * Query Assigned USER ROLE LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAllocatedList(SysUser user)
    {
        return userMapper.selectAllocatedList(user);
    }

    /**
     * Query Unassigned USER ROLE LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUnallocatedList(SysUser user)
    {
        return userMapper.selectUnallocatedList(user);
    }

    /**
     * By USER Name QUERY USER
     *
     * @param userName USER Name
     * @return USER Object INFORMATION
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * By USER ID QUERY USER
     *
     * @param userId USER ID
     * @return USER Object INFORMATION
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * Query the ROLE Group to which the USER Belongs
     *
     * @param userName USER Name
     * @return Result
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        if (CollectionUtils.isEmpty(list))
        {
            return StringUtils.EMPTY;
        }
        return list.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     *  Verify Whether USER Name Is Unique
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    public boolean checkUserNameUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkUserNameUnique(user.getUserName());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean checkGoogleUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkGoogleUnique(user.getGoogle());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     *  Verify Whether Mobile Phone Number Is Unique
     *
     * @param user  USER INFORMATION
     * @return
     */
    @Override
    public boolean checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     *  Verify Whether Email Is Unique
     *
     * @param user  USER INFORMATION
     * @return
     */
    @Override
    public boolean checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     *  Verify Whether the USER Is Allowed to Perform the OPERATION
     *
     * @param user  USER INFORMATION
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new ServiceException("OPERATION by Super MANAGEMENT Users Is Not Allowed");
        }
    }

    /**
     *  Verify Whether the USER Has Data Permission
     *
     * @param userId USER id
     */
    @Override
    public void checkUserDataScope(Long userId)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            SysUser user = new SysUser();
            user.setUserId(userId);
            user.setEnterpriseId(user.getEnterpriseId());
            List<SysUser> users = SpringUtils.getAopProxy(this).selectUserList(user);
            if (StringUtils.isEmpty(users))
            {
                throw new ServiceException("No permission to access user data!");
            }
        }
    }

    /**
     * ADDsave  USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user)
    {
        // ADD USER INFORMATION
        int rows = userMapper.insertUser(user);
        // Add the Association between USER and ROLE MANAGEMENT
        insertUserRole(user);
        return rows;
    }

    /**
     * Register USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    public boolean registerUser(SysUser user)
    {
        return userMapper.insertUser(user) > 0;
    }

    /**
     * MODIFYsave  USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // DELETE USER and ROLE Association
        if (user.getRoleIds()!=null){
            userRoleMapper.deleteUserRoleByUserId(userId);
        }
        // Add the Association between USER and ROLE MANAGEMENT
        insertUserRole(user);
        return userMapper.updateUser(user);
    }

    /**
     * USER Authorizes ROLE
     *
     * @param userId USER ID
     * @param roleIds ROLE Group
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds)
    {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * MODIFYUSER Status
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    public int updateUserStatus(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * MODIFY USER Basic INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    public boolean updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user) > 0;
    }

    /**
     * MODIFY USER Avatar
     *
     * @param userName USER Name
     * @param avatar Avatar Address
     * @return Result
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * RESETUSER Password
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * RESETUSER Password
     *
     * @param userName USER Name
     * @param password Password
     * @return Result
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * ADDUSER ROLE INFORMATION
     *
     * @param user USER Object
     */
    public void insertUserRole(SysUser user)
    {
        this.insertUserRole(user.getUserId(), user.getRoleIds());
    }

    /**
     * ADDUSER ROLE INFORMATION
     *
     * @param userId USER ID
     * @param roleIds ROLE Group
     */
    public void insertUserRole(Long userId, Long[] roleIds)
    {
        if (StringUtils.isNotEmpty(roleIds))
        {
            // Add the Association between USER and ROLE MANAGEMENT
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            userRoleMapper.batchUserRole(list);
        }
    }

    /**
     * By USER IDDELETEUSER
     *
     * @param userId USER ID
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId)
    {
        // DELETE USER and ROLE Association
        userRoleMapper.deleteUserRoleByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * Batch DELETE USER INFORMATION
     *
     * @param userIds USER ID to Be DELETED
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
            checkUserDataScope(userId);
        }
        // DELETE USER and ROLE Association
        userRoleMapper.deleteUserRole(userIds);
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * Import USER Data
     *
     * @param userList USER Data LIST
     * @param isUpdateSupport Whether Update Is Supported. If It Already Exists, Update the Data
     * @param operName OPERATIONUSER
     * @return Result
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new ServiceException("Import USER DataCANNOT BE EMPTY.！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SysUser user : userList)
        {
            try
            {
                // Verify Whether This USER Exists
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    BeanValidators.validateWithException(validator, user);
                    String password = configService.selectConfigByKey("sys.user.initPassword");
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    userMapper.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、Account " + user.getUserName() + " Import SUCCESS");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, user);
                    checkUserAllowed(u);
                    checkUserDataScope(u.getUserId());
                    user.setUserId(u.getUserId());
                    user.setUpdateBy(operName);
                    userMapper.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、Account " + user.getUserName() + " update success");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、Account " + user.getUserName() + "  Already Exists");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、Account " + user.getUserName() + " Import FAILURE：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "I'm sorry, Import FAILURE!" + failureNum + " The format of the data is incorrect, and the errors are as follows:");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "Congratulations! All data have been imported SUCCESSFULLY! A total of " + successNum + " records. The data are as follows:");
        }
        return successMsg.toString();
    }

    @Override
    public int resetPwdByCode(SysUser user) {
        return userMapper.updateUserByCode(user);
    }

    @Override
    public SysUser selectUserByGoogleId(String googleId) {
        return userMapper.selectUserByGoogleId(googleId);
    }

    @Override
    public SysUser selectUserByEid(String eid) {
        return userMapper.selectUserByEid(eid);
    }

    @Override
    public SysUser selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }



}
