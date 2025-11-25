package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ys.common.core.web.domain.BaseEntity;
import com.ys.common.core.xss.Xss;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Notice Announcement Table sys_notice
 *
 * @author ys
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysNotice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** Announcement ID */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    private String enterpriseId;

    /** Announcement Title */
    private String noticeTitle;

    /** Announcement Type (1 Notice 2 Announcement) */
    private String noticeType;

    /** Announcement Content */
    private String noticeContent;

    /** Announcement Status (0 Normal 1 Closed) */
    private String status;

    private String sendTos;

    /** Publication Time */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date publishTime;

    @TableField(exist = false)
    private Long[] sendTo;

    @TableField(exist = false)
    private String avatars;

    @TableField(exist = false)
    private String names;

    @TableField(exist = false)
    private Integer likeNum;

    @TableField(exist = false)
    private String isRead;

    @TableField(exist = false)
    private String isLiked;

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private Long uid;

    @TableField(exist = false)
    private String fullName;

    @Xss(message = "Announcement title cannot contain script characters")
    @NotBlank(message = "Announcement title cannot be empty.")
    @Size(min = 0, max = 200, message = "Announcement title cannot exceed 200 characters")
    public String getNoticeTitle() {
        return noticeTitle;
    }
}
