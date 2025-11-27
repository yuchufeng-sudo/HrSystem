package com.ys.system.service;

import com.ys.system.api.domain.SysUser;

import java.util.List;

/**
 * User  Business
 *
 * @author ys
 */
public interface ISysUserService
{
    /**
     *Query User list by Conditions with Pagination
     *
     * @param user  User Information
     * @return User INFORMATIONSet Information
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * Query Assigned User role list by Conditions with Pagination
     *
     * @param user  User Information
     * @return User INFORMATIONSet Information
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * Query Unassigned User role list by Conditions with Pagination
     *
     * @param user  User Information
     * @return User INFORMATIONSet Information
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * By User NameQUERYUSER
     *
     * @param userName User Name
     * @return User Object Information
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * By User IDQUERYUSER
     *
     * @param userId User ID
     * @return User Object Information
     */
    public SysUser selectUserById(Long userId);

    /**
     * According to User IDQuery the role Group to which the User Belongs
     *
     * @param userName User Name
     * @return Result
     */
    public String selectUserRoleGroup(String userName);

    /**
     *  Verify Whether User Name Is Unique
     *
     * @param user  User Information
     * @return Result
     */
    public boolean checkUserNameUnique(SysUser user);


    public boolean checkGoogleUnique(SysUser user);

    /**
     *  Verify Whether Mobile Phone Number Is Unique
     *
     * @param user  User Information
     * @return Result
     */
    public boolean checkPhoneUnique(SysUser user);

    /**
     *  Verify Whether Email Is Unique
     *
     * @param user  User Information
     * @return Result
     */
    public boolean checkEmailUnique(SysUser user);

    /**
     *  Verify Whether the User Is Allowed to Perform the OPERATION
     *
     * @param user  User Information
     */
    public void checkUserAllowed(SysUser user);

    /**
     *  Verify Whether the User Has Data Permission
     *
     * @param userId User id
     */
    public void checkUserDataScope(Long userId);

    /**
     * Add User Information
     *
     * @param user  User Information
     * @return Result
     */
    public int insertUser(SysUser user);

    /**
     * Register User Information
     *
     * @param user  User Information
     * @return Result
     */
    public boolean registerUser(SysUser user);

    /**
     * Update User Information
     *
     * @param user  User Information
     * @return Result
     */
    public int updateUser(SysUser user);

    /**
     * User Authorizes role
     *
     * @param userId User ID
     * @param roleIds role Group
     */
    public void insertUserAuth(Long userId, Long[] roleIds);

    /**
     * MODIFYUSER Status
     *
     * @param user  User Information
     * @return Result
     */
    public int updateUserStatus(SysUser user);

    /**
     * Update User Basic Information
     *
     * @param user  User Information
     * @return Result
     */
    public boolean updateUserProfile(SysUser user);

    /**
     * Update User Avatar
     *
     * @param userName User Name
     * @param avatar Avatar Address
     * @return Result
     */
    public boolean updateUserAvatar(String userName, String avatar);

    /**
     * RESETUSER Password
     *
     * @param user  User Information
     * @return Result
     */
    public int resetPwd(SysUser user);

    /**
     * RESETUSER Password
     *
     * @param userName User Name
     * @param password Password
     * @return Result
     */
    public int resetUserPwd(String userName, String password);

    /**
     * By User IDDELETEUSER
     *
     * @param userId User ID
     * @return Result
     */
    public int deleteUserById(Long userId);

    /**
     * Batch Delete User Information
     *
     * @param userIds User ID to Be DELETED
     * @return Result
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * Import User Data
     *
     * @param userList User Data list
     * @param isUpdateSupport Whether Update Is Supported. If It Already Exists, Update the Data
     * @param operName OPERATIONUSER
     * @return Result
     */
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    int resetPwdByCode(SysUser user);

    SysUser selectUserByGoogleId(String googleId);

    /**
     * According to Enterprise id Query
     * @param eid
     * @return
     */
    SysUser selectUserByEid(String eid);

    SysUser selectUserByEmail(String email);

}
