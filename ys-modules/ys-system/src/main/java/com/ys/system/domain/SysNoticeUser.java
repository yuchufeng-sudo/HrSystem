package com.ys.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ys.common.core.annotation.Excel;
import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class SysNoticeUser extends BaseEntity {
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

}
