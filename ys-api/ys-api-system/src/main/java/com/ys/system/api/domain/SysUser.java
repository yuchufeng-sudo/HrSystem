package com.ys.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.annotation.Excel.Type;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * User Object sys_user
 *
 */
public class SysUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** User ID */
    @TableId(type = IdType.AUTO)
    @Excel(name = "User Serial Number", type = Type.EXPORT, cellType = ColumnType.NUMERIC, prompt = "User  ID")
    private Long userId;

    /**  DEPARTMENT ID */
    @Excel(name = "Department ID", type = Type.IMPORT)
    private Long deptId;

    /** Enterprise ID */
    @Excel(name = "Enterprise Number" )
    private String enterpriseId;

    /** User Account */
    @Excel(name = "Login Name")
    private String userName;

    /** User nickname */
    @Excel(name = "User Name")
    private String nickName;

    /** User email */
    @Excel(name = "User email")
    private String email;

    /** User email */
    @TableField(exist = false)
    private String code;

    /** Mobile Phone Number */
    @Excel(name = "Mobile Phone Number", cellType = ColumnType.TEXT)
    private String phonenumber;

    /* MOBILE PHONE AREA CODE */
    private String phoneCode;

    /** Mobile Phone Number */
    @Excel(name = "Google Unique Identifier")
    private String google;

    /** User Gender */
    @Excel(name = "User Gender", readConverterExp = "0 = Male, 1 = Female, 2 = Unknown")
    private String sex;


    private String avatar;

    /** User  Type */
    private String userType;


    private String password;

    @Excel(name = "Account Status", readConverterExp = "0 = Normal, 1 = Disabled")
    private String status;

    /**DELETE flag (0 represents existence, 2 represents deletion) */
    private String delFlag;

    /** Finally Login IP */
    @Excel(name = "Finally Login IP", type = Type.EXPORT)
    private String loginIp;

    /** Finally Login  TIME  */
    @Excel(name = "Finally Login  TIME ", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Type.EXPORT)
    private Date loginDate;

    /** ROLEObject */
    private List<SysRole> roles;

    /** ROLE Group */
    private Long[] roleIds;

    /**  POSITION Group */
    private Long[] postIds;

    /** ROLEID */
    private Long roleId;

    private Character isFirst;

    public SysUser()
    {

    }

    public SysUser(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    @Xss(message = "User nickname cannot contain scripts Character ")
    @Size(min = 0, max = 30, message = "User length of the nickname cannot exceed 30 characters ")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Xss(message = "User Account Cannot contain scripts. Character ")
    @NotBlank(message = "User AccountCANNOT BE EMPTY.")
    @Size(min = 0, max = 30, message = "User Account LENGTH cannot exceed 30 Character ")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "The email format is incorrect.")
    @Size(min = 0, max = 50, message = "email LENGTH cannot exceed 30 Character ")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Size(min = 0, max = 11, message = "Mobile Phone NumberLENGTH cannot exceed 11 Character ")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public List<SysRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<SysRole> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("email", getEmail())
            .append("phonenumber", getPhonenumber())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("password", getPassword())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("loginIp", getLoginIp())
            .append("loginDate", getLoginDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Character getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Character isFirst) {
        this.isFirst = isFirst;
    }
}
