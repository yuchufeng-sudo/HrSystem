package com.ys.sync.service.impl;

import com.ys.sync.domain.LeaveResponse;
import com.ys.sync.service.ISyncLeaveService;
import com.ys.sync.utils.HttpUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Frank
 */
@Service
public class SyncLeaveServiceImpl implements ISyncLeaveService {

    private final HttpUtils httpUtils;

    @Autowired
    public SyncLeaveServiceImpl(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    @Override
    public LeaveResponse createLeave(String userId, String title, Date startAt, Date endAt, String reason) {
        if (userId == null || title == null || startAt == null || endAt == null || reason == null) {
            throw new IllegalArgumentException("Required parameters (user_id, title, start_at, end_at, reason) cannot be null");
        }
        List<NameValuePair> formParams = httpUtils.createFormParams();
        httpUtils.addFormParam(formParams, "user_id", userId);
        httpUtils.addFormParam(formParams, "title", title);
        httpUtils.addFormParam(formParams, "start_at", startAt);
        httpUtils.addFormParam(formParams, "end_at", endAt);
        httpUtils.addFormParam(formParams, "reason", reason);
        return httpUtils.callApi(
                "/leaves",
                HttpUtils.HttpMethod.POST,
                null,
                formParams,
                LeaveResponse.class
        );
    }

    @Override
    public LeaveResponse updateLeave(
            String id,
            String title,
            Date startAt,
            Date endAt,
            String reason,
            String allDay,
            Date approvedAt,
            String approvedBy,
            Date declinedAt,
            String declinedReason,
            Boolean isUnavailability)
    {
        if (id == null) {
            throw new IllegalArgumentException("Parameter 'id' is required for updating a leave");
        }

        List<NameValuePair> formParams = httpUtils.createFormParams();
        httpUtils.addFormParam(formParams, "id", id);
        httpUtils.addFormParam(formParams, "title", title);
        httpUtils.addFormParam(formParams, "start_at", startAt);
        httpUtils.addFormParam(formParams, "end_at", endAt);
        httpUtils.addFormParam(formParams, "reason", reason);
        httpUtils.addFormParam(formParams, "all_day", allDay);
        httpUtils.addFormParam(formParams, "approved_at", approvedAt);
        httpUtils.addFormParam(formParams, "approved_by", approvedBy);
        httpUtils.addFormParam(formParams, "declined_at", declinedAt);
        httpUtils.addFormParam(formParams, "declined_reason", declinedReason);
        httpUtils.addFormParam(formParams, "is_unavailability", isUnavailability);

        return httpUtils.callApi(
                "/leaves",
                HttpUtils.HttpMethod.PUT,
                null,
                formParams,
                LeaveResponse.class
        );
    }
}
