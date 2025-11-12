package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Hr settings entity hr_settings
 *
 * @author ys
 * @date 2025-05-28
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrSettings extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Primary key ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** Default workday start time */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Default workday start time", width = 30)
    private LocalTime defaultStartTime;
    /** Default workday end time */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Default workday end time", width = 30)
    private LocalTime defaultEndTime;
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Lunch Break Start Time", width = 30)
    private LocalTime restStartTime;
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "Lunch Break End Time", width = 30)
    private LocalTime restEndTime;
    /** Enterprise ID */
    @Excel(name = "Enterprise ID")
    private String enterpriseId;

    private String schedulingId;

    private Character physicalLocations;
    private Character schedulingManagersfor;
    private String selectAreas;
    private Integer gracePeriod;
    private Character enableOvertime;
    private Integer maximumOvertime;

}
