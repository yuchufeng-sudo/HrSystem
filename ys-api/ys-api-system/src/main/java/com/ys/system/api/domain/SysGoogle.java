package com.ys.system.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.Data;


@Data
public class SysGoogle extends BaseEntity {
    /** USER ID */
    @TableId(type = IdType.AUTO)
    private Long userId;
    /** Mobile Phone Number */
    @Excel(name = "Google Unique Identifier")
    private String google;

    private String username;

    private String password;

    private String email;
}
