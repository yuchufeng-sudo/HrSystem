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

    /**
     * Query Leave Application   list
     *
     * @param hrLeave  Leave Application
     * @return  Leave Application Set
     */
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    HrLeave selectHrLeaveLastTime(Long userId, Date stateTime , String leaveType);

    int updateByLeaveId(HrLeave hrLeave);

    Map leaveCount(HrLeave hrLeave);

    Long selectLeader(Long userId);

    Map leaveCountByUser(HrLeave hrLeave);

    /**
     * Count the number of days of Employee Leave Application
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> selectLeaveTotal(String userEnterpriseId);
}
