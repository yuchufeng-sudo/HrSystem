package com.ys.sync.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * @description: Leave
 * @author: xz_Frank
 * @date: 2025/10/30
 */
@Data
public class LeaveResponse {
    private String id;
    @JsonProperty("user_id")
    private String userId;
    private String title;
    @JsonProperty("start_at")
    private ZonedDateTime startAt;
    @JsonProperty("end_at")
    private ZonedDateTime endAt;
    private String reason;
    @JsonProperty("all_day")
    private String allDay;
    @JsonProperty("approved_at")
    private ZonedDateTime approvedAt;
    @JsonProperty("approved_by")
    private String approvedBy;
    @JsonProperty("declined_at")
    private ZonedDateTime declinedAt;
    @JsonProperty("declined_reason")
    private String declinedReason;
    @JsonProperty("external_ids")
    private List<ExternalId> externalIds;
    @JsonProperty("is_unavailability")
    private boolean isUnavailability;
}
