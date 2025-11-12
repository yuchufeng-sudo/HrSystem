package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrClockRecord;

import java.util.List;

/**
 *  Clock in RecordMapper Interface
 *
 * @author ys
 * @date 2024-11-29
 */
public interface HrClockRecordMapper extends BaseMapper<HrClockRecord>
{
    /**
     * Query the Clock in Record list
     *
     * @param oaClockRecord  Clock in Record
     * @return  Clock in RecordSet
     */
    public List<HrClockRecord> selectOaClockRecordList(HrClockRecord oaClockRecord);

    void replaceBatch(List<HrClockRecord> oaClockRecords);


    void saveData(HrClockRecord clockRecord);
}
