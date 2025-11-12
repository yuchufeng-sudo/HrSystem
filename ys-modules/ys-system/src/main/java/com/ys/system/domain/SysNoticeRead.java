package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * notice User entity sys_notice_user
 *
 * @author ys
 * @date 2025-06-05
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysNoticeRead extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type =  IdType.AUTO )
    private Long id;
    /** notice ID */
    @Excel(name = "notice ID")
    private Long noticeId;
    /** user ID */
    @Excel(name = "user ID")
    private Long userId;
    /** read */
    @Excel(name = "read")
    private String isRead;
    /** liked */
    @Excel(name = "liked")
    private String isLiked;
    /** like Time */
    private Date likeTime;

}
