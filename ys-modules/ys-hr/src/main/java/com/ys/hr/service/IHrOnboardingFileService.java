package com.ys.hr.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrOnboardingFile;

/**
 * Onboarding File Service Interface
 *
 * @author ys
 * @date 2025-10-13
 */
public interface IHrOnboardingFileService extends IService<HrOnboardingFile>
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

    /**
     * Add Onboarding File
     *
     * @param hrOnboardingFile Onboarding File
     * @return Result
     */
    public int insertHrOnboardingFile(HrOnboardingFile hrOnboardingFile);

    /**
     * Update Onboarding File
     *
     * @param hrOnboardingFile Onboarding File
     * @return Result
     */
    public int updateHrOnboardingFile(HrOnboardingFile hrOnboardingFile);

    /**
     * Batch delete Onboarding File
     *
     * @param ids Onboarding File primary keys to be deleted
     * @return Result
     */
    public int deleteHrOnboardingFileByIds(String[] ids);

    /**
     * Delete Onboarding File information
     *
     * @param id Onboarding File primary key
     * @return Result
     */
    public int deleteHrOnboardingFileById(String id);

    List<HrOnboardingFile> selectHrOnboardingFileListByEmp(HrOnboardingFile hrOnboardingFile);
}
