package com.ys.hr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *  Training Management entity hr_training_programs
 *
 * @author ys
 * @date 2025-05-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrTrainingPrograms extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "program_id", type =  IdType.AUTO )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long programId;
    /** title */
    @Excel(name = "Course & Certification")
    private String title;
    /** Training type */
    @Excel(name = "Type", readConverterExp = "1=Course,2=Certifications")
    private String programType;
    /** department */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;
    @TableField(exist = false)
    @Excel(name = "department")
    private String deptName;
    /** Certification issuer */
    private String issuingOrg;
    /** Assigned employees count */
    @Excel(name = "Assigned count")
    private Integer assignedCount;
    /** status */
    @Excel(name = "Status")
    private String status;
    /** Attachment url */
    @Excel(name = "Attachment url")
    private String attachmentUrl;
    @Excel(name = "Duration (minutes)")
    private String duration;
    /** Completion deadline */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Completion deadline", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deadline;
    /** Assignment priority */
    @Excel(name = "Assignment priority", readConverterExp = "1=Low,2=Normal,3=High")
    private String priority;
    /** Enable employee notification */
    @Excel(name = "Enable employee notification", readConverterExp = "1=true,2=false")
    private String notifyEmployee;
    /** Enable deadline reminder */
    @Excel(name = "Enable deadline reminder", readConverterExp = "1=true,2=false")
    private String sendReminder;
    /** Enable manager notification */
    @Excel(name = "Enable manager notification", readConverterExp = "1=true,2=false")
    private String notifyManager;

    private String enterpriseId;

    @TableField(exist = false)
    private String[] employeeIds;

    public String getStatus(){
        if (this.deadline!=null){
            LocalDate dateToCheck = this.deadline.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();
            if (dateToCheck.isBefore(today)) {
                return "FINISH";
            }else {
                return this.status;
            }
        }else{
            return this.status;
        }
    }
}
