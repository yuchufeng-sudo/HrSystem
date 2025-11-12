package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrLeave;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  LEAVE APPLICATION Service Interface
 *
 * @author ys
 * @date 2025-05-21
 */
public interface IHrLeaveService extends IService<HrLeave>
{

    /**
     * QUERY LEAVE APPLICATION   LIST
     *
     * @param hrLeave  LEAVE APPLICATION 
     * @return  LEAVE APPLICATION Set
     */
    public List<HrLeave> selectHrLeaveList(HrLeave hrLeave);

    HrLeave selectHrLeaveLastTime(Long userId, Date stateTime , String leaveType);

    int updateByLeaveId(HrLeave hrLeave);

    Map leaveCount(HrLeave hrLeave);

    Long selectLeader(Long userId);

    Map leaveCountByUser(HrLeave hrLeave);

    /**
     * Count the number of days of EMPLOYEE LEAVE APPLICATION
     * @param userEnterpriseId
     * @return
     */
    Map<String, Object> selectLeaveTotal(String userEnterpriseId);
}
