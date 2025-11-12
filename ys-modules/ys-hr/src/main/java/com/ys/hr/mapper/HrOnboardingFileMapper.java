package com.ys.hr.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrOnboardingFile;

/**
 * Onboarding File Mapper Interface
 *
 * @author ys
 * @date 2025-10-13
 */
public interface HrOnboardingFileMapper extends BaseMapper<HrOnboardingFile>
{
    /**
     * Query Onboarding File
     *
     * @param id Onboarding File primary key
     * @return Onboarding File
     */
    public HrOnboardingFile selectHrOnboardingFileById(String id);

    /**
     * Query Onboarding File list
     *
     * @param hrOnboardingFile Onboarding File
     * @return Onboarding File collection
     */
    public List<HrOnboardingFile> selectHrOnboardingFileList(HrOnboardingFile hrOnboardingFile);

    List<HrOnboardingFile> selectHrOnboardingFileListByEmp(HrOnboardingFile hrOnboardingFile);
}
