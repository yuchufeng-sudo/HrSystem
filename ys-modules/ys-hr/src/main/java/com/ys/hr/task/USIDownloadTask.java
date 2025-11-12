package com.ys.hr.task;

import com.ys.hr.domain.HrSuperUsiInfo;
import com.ys.hr.service.IHrSuperUsiInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/23
 */
@Service
public class USIDownloadTask {

    @Resource
    private IHrSuperUsiInfoService superUsiInfoService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void downloadUSIData() throws IOException {
        List<HrSuperUsiInfo> hrSuperUsiInfos = superUsiInfoService.downloadAndParseUSIFile();
        superUsiInfoService.batchSaveOrUpdate(hrSuperUsiInfos);
    }
}
