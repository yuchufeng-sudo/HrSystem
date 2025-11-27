package com.ys.sync.service;

import com.ys.sync.domain.LeaveResponse;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Frank
 */
public interface ISyncLeaveService {

    /**
     * Create leave record
     *
     * @param userId         User ID (required)
     * @param title          Leave title (required)
     * @param startAt        Start time (required, ISO 8601 with time zone)
     * @param endAt          End time (required, ISO 8601 with time zone)
     * @param reason         Leave reason (required)
     * @return Leave information returned by the interface
     */
    public LeaveResponse createLeave(String userId, String title, Date startAt, Date endAt, String reason);

    /**
     * Update leave record (corresponding to the Update a leave interface)
     *
     * @param id              Leave ID (required, used to locate the record to be updated)
     * @param title           Optional: Leave title
     * @param startAt         Optional: Start time (ISO 8601 with time zone)
     * @param endAt           Optional: End time (ISO 8601 with time zone)
     * @param reason          Optional: Leave reason
     * @param allDay          Optional: Whether it is all day
     * @param approvedAt      Optional: Approval time
     * @param approvedBy      Optional: Approver
     * @param declinedAt      Optional: Rejection time
     * @param declinedReason  Optional: Rejection reason
     * @param isUnavailability Optional: Whether it is in unavailable state (default false)
     * @return Updated leave information
     */
    LeaveResponse updateLeave(
            String id, // Unique required parameter
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
