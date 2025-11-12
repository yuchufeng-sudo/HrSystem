package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrInviteCode;
import com.ys.hr.domain.HrInviteRequests;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  INVITATION LINK REQUEST HANDLING Mapper Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface HrInviteRequestsMapper extends BaseMapper<HrInviteRequests>
{
    /**
     * QUERY INVITATION LINK REQUEST HANDLING   LIST
     *
     * @param hrInviteRequests  INVITATION LINK REQUEST HANDLING
     * @return  INVITATION LINK REQUEST HANDLING Set
     */
    public List<HrInviteRequests> selectHrInviteRequestsList(HrInviteRequests hrInviteRequests);

    HrInviteCode selectInviteCodeStatus(String code);

    void insertInviteCode(@Param("enterpriseId") String enterpriseId,@Param("code") String code);

    Integer updateInviteCodeStatus(String code);
}
