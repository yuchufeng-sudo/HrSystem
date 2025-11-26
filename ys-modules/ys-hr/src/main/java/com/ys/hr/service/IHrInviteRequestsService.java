package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrInviteCode;
import com.ys.hr.domain.HrInviteRequests;

import java.util.List;

/**
 *  Invitation link request Service Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface IHrInviteRequestsService extends IService<HrInviteRequests>
{

    /**
     * Query Invitation link request   list
     *
     * @param hrInviteRequests  Invitation link request
     * @return Invitation link request Set
     */
    public List<HrInviteRequests> selectHrInviteRequestsList(HrInviteRequests hrInviteRequests);

    boolean acceptHrInviteRequests(List<HrInviteRequests> hrInviteRequests);

    boolean declineHrInviteRequests(Long[] requestIds);

    HrInviteCode selectInviteCodeStatus(String code);

    String getInviteCode(String enterpriseId);

    Integer updateInviteCodeStatus(String code);
}
