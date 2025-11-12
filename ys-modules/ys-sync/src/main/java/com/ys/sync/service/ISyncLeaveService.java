package com.ys.sync.service;

import com.ys.sync.domain.LeaveResponse;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Frank
 */
public interface ISyncLeaveService {

    /**
     * 创建请假记录
     *
     * @param userId         用户ID（必填）
     * @param title          请假标题（必填）
     * @param startAt        开始时间（必填，ISO 8601带时区）
     * @param endAt          结束时间（必填，ISO 8601带时区）
     * @param reason         请假原因（必填）
     * @return 接口返回的请假信息
     */
    public LeaveResponse createLeave(String userId, String title, Date startAt, Date endAt, String reason);

    /**
     * 更新请假记录（对应Update a leave接口）
     *
     * @param id              请假ID（必填，用于定位要更新的记录）
     * @param title           可选：请假标题
     * @param startAt         可选：开始时间（ISO 8601带时区）
     * @param endAt           可选：结束时间（ISO 8601带时区）
     * @param reason          可选：请假原因
     * @param allDay          可选：是否全天
     * @param approvedAt      可选：批准时间
     * @param approvedBy      可选：批准人
     * @param declinedAt      可选：拒绝时间
     * @param declinedReason  可选：拒绝原因
     * @param isUnavailability 可选：是否为不可用状态（默认false）
     * @return 更新后的请假信息
     */
    LeaveResponse updateLeave(
            String id, // 唯一必填参数
            String title,
            Date startAt,
            Date endAt,
            String reason,
            String allDay,
            Date approvedAt,
            String approvedBy,
            Date declinedAt,
            String declinedReason,
            Boolean isUnavailability
    );
}
