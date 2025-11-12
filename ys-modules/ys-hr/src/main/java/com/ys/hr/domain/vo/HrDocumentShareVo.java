package com.ys.hr.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Document Management entity hr_document
 *
 * @author ys
 * @date 2025-05-27
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrDocumentShareVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Document ID */
    @TableId(value = "document_id", type =  IdType.AUTO )
    @JsonSerialize(using = ToStringSerializer.class)
    private Long documentId;
    /** Document display name */
    private String documentName;
    /** Original file name */
    private String fileName;
    /** File storage path */
    private String filePath;
    /** File type (PDF, DOCX, etc) */
    private String fileType;
    /** File size in bytes */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fileSize;
    /** User ID who uploaded the document */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uploadUserId;
    /** Employee ID who uploaded the document */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long uploadCandidateId;
    /** Document upload date */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date uploadDate;
    /** Document description */
    private String description;
    /** Document status (0-disabled, 1-enabled) */
    private String status;

    private String uploadAvatar;

    private String uploadFullName;

    private String uploadEmail;

    private String permissionType;

}
