package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpHoliday;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Employee Holiday Mapper Interface
 *
 * @author ys
 * @date 2025-05-23
 */
public interface HrEmpHolidayMapper extends BaseMapper<HrEmpHoliday> {
    /**
     * Query Employee Holiday list
     *
     * @param hrEmpHoliday Employee Holiday
     * @return Employee Holiday Set
     */
    public List<HrEmpHoliday> selectHrEmpHolidayList(HrEmpHoliday hrEmpHoliday);

    HrEmpHoliday selectHrEmpHolidayById(Long empleHolidayId);

    int updateByHrEmpHolidayById(HrEmpHoliday hrEmpHoliday);

    Map<String, Object> getCount(HrEmpHoliday hrEmpHoliday);

    /**
     * Query holiday information
     *
     * @param userId
     * @param attendanceTime
     * @return
     */
    HrEmpHoliday selectHolidayByUserId(@Param("userId") Long userId,
            @Param("attendanceTime") Date attendanceTime);

    /**
     * Query if there was a holiday yesterday
     *
     * @param userEnterpriseId
     * @return
     */
    Integer selectHolidayByLastDay(String userEnterpriseId);

    /**
     * Query if there is a holiday today
     *
     * @param userEnterpriseId
     * @return
     */
    Integer selectHolidayByThisDay(String userEnterpriseId);

    /**
     * Query today is a holiday list
     *
     * @param enterpriseId
     * @return
     */
    List<HrEmpHoliday> selectHrEmpHolidayListBYToday(String enterpriseId);

    /**
     * Check one month's vacation status
     * @param enterpriseId
     * @param searchData
     * @return
     */
    List<HrEmpHoliday> selectByUserIdAndDate(@Param("enterpriseId") String enterpriseId,
                                             @Param("searchData") Date searchData);
}
