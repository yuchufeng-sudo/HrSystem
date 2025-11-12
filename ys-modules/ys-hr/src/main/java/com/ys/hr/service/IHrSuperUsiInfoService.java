package com.ys.hr.service;

import java.io.IOException;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrSuperUsiInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * USI and product information table Service Interface
 *
 * @author ys
 * @date 2025-10-23
 */
public interface IHrSuperUsiInfoService extends IService<HrSuperUsiInfo>
{

    /**
     * Query USI and product information table list
     *
     * @param hrSuperUsiInfo USI and product information table
     * @return USI and product information table collection
     */
    List<HrSuperUsiInfo> selectHrSuperUsiInfoList(HrSuperUsiInfo hrSuperUsiInfo);

    void batchSaveOrUpdate(List<HrSuperUsiInfo> entityList);

    List<HrSuperUsiInfo> downloadAndParseUSIFile() throws IOException;
}
