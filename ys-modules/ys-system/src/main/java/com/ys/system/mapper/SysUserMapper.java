package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User table data layer
 *
 * @author ruoyi
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
     * Query Unassigned USER ROLE LIST by Conditions with Pagination
     *
     * @param user USER INFORMATION
     * @return USER INFORMATIONSet INFORMATION
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
     * ADD USER INFORMATION
     *
     * @param user USER INFORMATION
     * @return Result
     */
    public int insertUser(SysUser user);

    /**
     * MODIFY USER INFORMATION
     *
     * @param user USER INFORMATION
     * @return Result
     */
    public int updateUser(SysUser user);

    /**
     * MODIFY USER Avatar
     *
     * @param userName USER Name
     * @param avatar   Avatar Address
     * @return Result
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * RESETUSER Password
     *
     * @param userName USER Name
     * @param password Password
     * @return Result
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

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
     * Verify Whether USER Name Is Unique
     *
     * @param userName USER Name
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
     * @param email USER email
     * @return Result
     */
    public SysUser checkEmailUnique(String email);

    int updateUserByCode(SysUser user);

    SysUser selectUserByGoogleId(String googleId);

    SysUser selectUserByEid(String eid);

    SysUser selectUserByEmail(String email);

    SysUser checkGoogleUnique(String google);
}
