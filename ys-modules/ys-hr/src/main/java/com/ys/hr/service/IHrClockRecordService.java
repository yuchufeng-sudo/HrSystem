package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrClockRecord;

import java.util.List;

/**
 *  Clock in RecordService Interface
 *
 * @author ys
 * @date 2024-11-29
 */
public interface IHrClockRecordService extends IService<HrClockRecord>
{

    /**
     * Query the Clock in Record list
     *
     * @param HrClockRecord  Clock in Record
     * @return  Clock in RecordSet
     */
    public List<HrClockRecord> selectHrClockRecordList(HrClockRecord HrClockRecord);

    void replaceBatch(List<HrClockRecord> HrClockRecords);

    /**
     * Add Clock in Record
     * @param clockRecord
     */
    void addRecord(HrClockRecord clockRecord);
}
