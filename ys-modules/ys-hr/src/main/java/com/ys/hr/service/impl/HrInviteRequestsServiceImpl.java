package com.ys.hr.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrInviteCode;
import com.ys.hr.domain.HrInviteRequests;
import com.ys.hr.mapper.HrInviteRequestsMapper;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.IHrInviteRequestsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *  Invitation link request Service Business Layer Processing
 *
 * @author ys
 * @date 2025-05-21
 */
@Service
public class HrInviteRequestsServiceImpl extends ServiceImpl<HrInviteRequestsMapper, HrInviteRequests> implements IHrInviteRequestsService
{

    @Resource
    private IHrEmployeesService employeesService;

    /**
     * Query Invitation link request   list
     *
     *
     * @param hrInviteRequests  Invitation link request
     * @return Invitation link request
     */
    @Override
    public List<HrInviteRequests> selectHrInviteRequestsList(HrInviteRequests hrInviteRequests)
    {
        return baseMapper.selectHrInviteRequestsList(hrInviteRequests);
    }

    @Override
    @Transactional
    public boolean acceptHrInviteRequests(List<HrInviteRequests> hrInviteRequests) {
        for (HrInviteRequests hrInviteRequest : hrInviteRequests) {
            hrInviteRequest.setStatus("1");
            HrEmployees hrEmployees = new HrEmployees();
            hrEmployees.setFullName(hrInviteRequest.getFullName());
            hrEmployees.setEmail(hrInviteRequest.getEmail());
            hrEmployees.setUsername(hrInviteRequest.getUsername());
            hrEmployees.setPhoneCode(hrInviteRequest.getPhoneCode());
            hrEmployees.setContactPhone(hrInviteRequest.getContactPhone());
            hrEmployees.setAccessLevel(hrInviteRequest.getAccessLevel());
            hrEmployees.setPassword(hrInviteRequest.getPassword());
            hrEmployees.setEnterpriseId(hrInviteRequest.getEnterpriseId());
            hrEmployees.setCountry(hrInviteRequest.getCountry());
            hrEmployees.setAddress1(hrInviteRequest.getAddress1());
            hrEmployees.setAddress2(hrInviteRequest.getAddress2());
            hrEmployees.setCity(hrInviteRequest.getCity());
            hrEmployees.setProvince(hrInviteRequest.getProvince());
            hrEmployees.setPostCode(hrInviteRequest.getPostCode());
            employeesService.insertEmployees(hrEmployees);
        }
        return this.updateBatchById(hrInviteRequests);
    }

    @Override
    @Transactional
    public boolean declineHrInviteRequests(Long[] requestIds) {
        List<HrInviteRequests> hrInviteRequests = new ArrayList<>();
        for (Long requestId : requestIds) {
            HrInviteRequests inviteRequests = new HrInviteRequests();
            inviteRequests.setRequestId(requestId);
            inviteRequests.setStatus("2");
            hrInviteRequests.add(inviteRequests);
        }
        return this.updateBatchById(hrInviteRequests);
    }

    @Override
    public HrInviteCode selectInviteCodeStatus(String code){
        return baseMapper.selectInviteCodeStatus(code);
    }

    @Override
    public String getInviteCode(String enterpriseId){
        String code = IdUtil.fastSimpleUUID();
        baseMapper.insertInviteCode(enterpriseId,code);
        return code;
    }

    @Override
    public Integer updateInviteCodeStatus(String code){
        return baseMapper.updateInviteCodeStatus(code);
    }
}
