package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User table data layer
 *
 * @author ys
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * Query user list by conditions with pagination
     *
     * @param sysUser User information
     * @return User information set
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * Query assigned user role list by conditions with pagination
     *
     * @param user User information
     * @return User information set
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * Query Unassigned User role list by Conditions with Pagination
     *
     * @param user User Information
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
     * Add User Information
     *
     * @param user User Information
     * @return Result
     */
    public int insertUser(SysUser user);

    /**
     * Update User Information
     *
     * @param user User Information
     * @return Result
     */
    public int updateUser(SysUser user);

    /**
     * Update User Avatar
     *
     * @param userName User Name
     * @param avatar   Avatar Address
     * @return Result
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * RESETUSER Password
     *
     * @param userName User Name
     * @param password Password
     * @return Result
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

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
     * Verify Whether User Name Is Unique
     *
     * @param userName User Name
     * @return Result
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * Verify Whether Mobile Phone Number Is Unique
     *
     * @param phonenumber Mobile Phone Number
     * @return Result
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * Verify Whether Email Is Unique
     *
     * @param email User email
     * @return Result
     */
    public SysUser checkEmailUnique(String email);

    int updateUserByCode(SysUser user);

    SysUser selectUserByGoogleId(String googleId);

    SysUser selectUserByEid(String eid);

    SysUser selectUserByEmail(String email);

    SysUser checkGoogleUnique(String google);
}
