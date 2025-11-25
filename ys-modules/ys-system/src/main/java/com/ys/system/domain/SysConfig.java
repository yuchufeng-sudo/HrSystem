package com.ys.system.domain;

import com.ys.common.core.annotation.Excel;
import com.ys.common.core.annotation.Excel.ColumnType;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Config configuration table sys_config
 *
 * @author ys
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Config primary key */
    @Excel(name = "Config primary key", cellType = ColumnType.NUMERIC)
    private Long configId;

    /** Config Name */
    @Excel(name = "Config Name")
    @NotBlank(message = "Config Name cannot be empty.")
    @Size(min = 0, max = 100, message = "Config Name cannot exceed 100 characters.")
    private String configName;

    /** Config keyName */
    @Excel(name = "Config keyName")
    @NotBlank(message = "Config keyName cannot be empty.")
    @Size(min = 0, max = 100, message = "Config keyName cannot exceed 100 characters.")
    private String configKey;

    /** Config Key-Value */
    @Excel(name = "Config Key-Value")
    @NotBlank(message = "Config Key-Value cannot be empty.")
    @Size(min = 0, max = 500, message = "Config Key-Value cannot exceed 500 characters.")
    private String configValue;

    /** System built-in (Y=Yes N=No) */
    @Excel(name = "System built-in", readConverterExp = "Y=Yes,N=No")
    private String configType;
}
