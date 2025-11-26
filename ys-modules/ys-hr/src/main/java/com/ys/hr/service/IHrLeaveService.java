package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrLeave;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  Leave Application Service Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface IHrLeaveService extends IService<HrLeave>
{

    List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    HrLeave selectHrLeaveLastTime(Long userId, Date stateTime , String leaveType);

    int updateByLeaveId(HrLeave hrLeave);

    Map leaveCount(HrLeave hrLeave);

    Long selectLeader(Long userId);

    Map leaveCountByUser(HrLeave hrLeave);

    Map<String, Object> selectLeaveTotal(String userEnterpriseId);
}
