package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrInviteCode;
import com.ys.hr.domain.HrInviteRequests;

import java.util.List;

/**
 *  INVITATION LINK REQUEST HANDLING Service Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface IHrInviteRequestsService extends IService<HrInviteRequests>
{

    /**
     * QUERY INVITATION LINK REQUEST HANDLING   LIST
     *
     * @param hrInviteRequests  INVITATION LINK REQUEST HANDLING
     * @return  INVITATION LINK REQUEST HANDLING Set
     */
    public List<HrInviteRequests> selectHrInviteRequestsList(HrInviteRequests hrInviteRequests);

    boolean acceptHrInviteRequests(List<HrInviteRequests> hrInviteRequests);

    boolean declineHrInviteRequests(Long[] requestIds);

    HrInviteCode selectInviteCodeStatus(String code);

    String getInviteCode(String enterpriseId);

    Integer updateInviteCodeStatus(String code);
}
