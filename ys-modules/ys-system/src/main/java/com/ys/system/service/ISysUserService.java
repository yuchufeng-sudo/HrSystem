package com.ys.system.service;

import com.ys.system.api.domain.SysUser;

import java.util.List;

/**
 * USER  Business
 *
 * @author ruoyi
 */
public interface ISysUserService
{
    /**
     *Query USER LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * Query Assigned USER ROLE LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * Query Unassigned USER ROLE LIST by Conditions with Pagination
     *
     * @param user  USER INFORMATION
     * @return  USER INFORMATIONSet INFORMATION
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * By USER NameQUERYUSER
     *
     * @param userName USER Name
     * @return USER Object INFORMATION
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * By USER IDQUERYUSER
     *
     * @param userId USER ID
     * @return USER Object INFORMATION
     */
    public SysUser selectUserById(Long userId);

    /**
     * According to USER IDQuery the ROLE Group to which the USER Belongs
     *
     * @param userName USER Name
     * @return Result
     */
    public String selectUserRoleGroup(String userName);

    /**
     *  Verify Whether USER Name Is Unique
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public boolean checkUserNameUnique(SysUser user);


    public boolean checkGoogleUnique(SysUser user);

    /**
     *  Verify Whether Mobile Phone Number Is Unique
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     *  Verify Whether Email Is Unique
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     *  Verify Whether the USER Is Allowed to Perform the OPERATION
     *
     * @param user  USER INFORMATION
     */
    public void checkUserAllowed(SysUser user);

    /**
     *  Verify Whether the USER Has Data Permission
     *
     * @param userId USER id
     */
    public void checkUserDataScope(Long userId);

    /**
     * ADD USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public int insertUser(SysUser user);

    /**
     * Register USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public boolean registerUser(SysUser user);

    /**
     * MODIFY USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public int updateUser(SysUser user);

    /**
     * USER Authorizes ROLE
     *
     * @param userId USER ID
     * @param roleIds ROLE Group
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * MODIFYUSER Status
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public int updateUserStatus(SysUser user);

    /**
     * MODIFY USER Basic INFORMATION
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public boolean updateUserProfile(SysUser user);

    /**
     * MODIFY USER Avatar
     *
     * @param userName USER Name
     * @param avatar Avatar Address
     * @return Result
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * RESETUSER Password
     *
     * @param user  USER INFORMATION
     * @return Result
     */
    public int resetPwd(SysUser user);

    /**
     * RESETUSER Password
     *
     * @param userName USER Name
     * @param password Password
     * @return Result
     */
    public int resetUserPwd(String userName, String password);

    /**
     * By USER IDDELETEUSER
     *
     * @param userId USER ID
     * @return Result
     */
    public int deleteUserById(Long userId);

    /**
     * Batch DELETE USER INFORMATION
     *
     * @param userIds USER ID to Be DELETED
     * @return Result
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * Import USER Data
     *
     * @param userList USER Data LIST
     * @param isUpdateSupport Whether Update Is Supported. If It Already Exists, Update the Data
     * @param operName OPERATIONUSER
     * @return Result
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    int resetPwdByCode(SysUser user);

    SysUser selectUserByGoogleId(String googleId);

    /**
     * According to ENTERPRISE id QUERY
     * @param eid
     * @return
     */
    SysUser selectUserByEid(String eid);

    SysUser selectUserByEmail(String email);

}
