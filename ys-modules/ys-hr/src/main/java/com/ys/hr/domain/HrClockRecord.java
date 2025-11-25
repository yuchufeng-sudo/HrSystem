package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Clock in RecordObject hr_clock_record
 *
 * @author ys
 * @date 2024-11-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrClockRecord {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** Name */
    @Excel(name = "Name")
    private String name;
    /** Employee ID */
    @Excel(name = "Employee ID")
    private String jobnumber;
    /** Clock in time */
    @Excel(name = " Clock in time")
    private Integer time;
    /** Door Opening Type: 1: Face 2: Card 3: Password 4: QR Code 5: Fingerprint */
    @Excel(name = "Door Opening Type")
    private String type;

    /** Body Temperature */
    @Excel(name = "Body Temperature")
    private String temperature;

    private String enterpriseId;

    @TableField(exist = false)
    private Integer passnum;

    @TableField(exist = false)
    private String pic;

    @TableField(exist = false)
    private String image;

}
