package com.ys.hr.service.impl;

import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingFileMapper;
import com.ys.hr.domain.HrOnboardingFile;
import com.ys.hr.service.IHrOnboardingFileService;
import com.ys.common.core.utils.DateUtils;
import java.util.Arrays;

/**
 * Onboarding File Service Implementation
 *
 * @author ys
 * @date 2025-10-13
 */
@Slf4j
@Service
public class HrOnboardingFileServiceImpl extends ServiceImpl<HrOnboardingFileMapper, HrOnboardingFile> implements IHrOnboardingFileService
{

    /**
     * Query Onboarding File
     *
     * @param id Onboarding File primary key
     * @return Onboarding File
     */
    @Override
    public HrOnboardingFile selectHrOnboardingFileById(String id)
    {
        return baseMapper.selectHrOnboardingFileById(id);
    }

    /**
     * Query Onboarding File list
     *
     * @param hrOnboardingFile Onboarding File
     * @return Onboarding File
     */
    @Override
    public List<HrOnboardingFile> selectHrOnboardingFileList(HrOnboardingFile hrOnboardingFile)
    {
        return baseMapper.selectHrOnboardingFileList(hrOnboardingFile);
    }

    /**
     * Add Onboarding File
     *
     * @param hrOnboardingFile Onboarding File
     * @return Result
     */
    @Override
    public int insertHrOnboardingFile(HrOnboardingFile hrOnboardingFile)
    {
        hrOnboardingFile.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrOnboardingFile);
    }

    /**
     * Update Onboarding File
     *
     * @param hrOnboardingFile Onboarding File
     * @return Result
     */
    @Override
    public int updateHrOnboardingFile(HrOnboardingFile hrOnboardingFile)
    {
        if (hrOnboardingFile == null || hrOnboardingFile.getId() == null) {
            throw new IllegalArgumentException("Invalid onboarding file");
        }

        log.info("Updating onboarding file: {}", hrOnboardingFile.getId());
        hrOnboardingFile.setUpdateTime(DateUtils.getNowDate());

        int result = baseMapper.updateById(hrOnboardingFile);
        if (result == 0) {
            log.warn("No onboarding file found with id: {}", hrOnboardingFile.getId());
        }
        return result;
    }

    /**
     * Batch delete Onboarding File
     *
     * @param ids Onboarding File primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrOnboardingFileByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * Delete Onboarding File information
     *
     * @param id Onboarding File primary key
     * @return Result
     */
    @Override
    public int deleteHrOnboardingFileById(String id)
    {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<HrOnboardingFile> selectHrOnboardingFileListByEmp(HrOnboardingFile hrOnboardingFile) {
        return baseMapper.selectHrOnboardingFileListByEmp(hrOnboardingFile);
    }
}
