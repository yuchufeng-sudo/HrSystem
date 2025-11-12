package com.ys.sync.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import javax.validation.constraints.Size;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.annotation.Excel;
import javax.validation.constraints.NotNull;

/**
 * sync shiftcare user entity sync_shiftcare_user
 *
 * @author ys
 * @date 2025-10-30
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncShiftcareUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.ASSIGN_UUID )
    private String id;
    /** username */
    @Excel(name = "username")
    private String username;
    /** password */
    @Excel(name = "password")
    private String password;
    /** user ID */
    @Excel(name = "user ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /** shiftcare user id */
    @Excel(name = "shiftcare user id")
    private String shiftcareUserId;

}
