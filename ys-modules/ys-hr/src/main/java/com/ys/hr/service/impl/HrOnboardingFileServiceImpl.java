package com.ys.hr.service.impl;

import java.util.List;
import java.util.Arrays;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.DatabaseOperationException;
import com.ys.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrOnboardingFileMapper;
import com.ys.hr.domain.HrOnboardingFile;
import com.ys.hr.service.IHrOnboardingFileService;
import com.ys.common.core.utils.DateUtils;

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
        // 参数校验：主键不能为空
        if (id == null || id.trim().isEmpty()) {
            log.error("Query onboarding file failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding file ID cannot be null or empty");
        }

        try {
            HrOnboardingFile file = baseMapper.selectHrOnboardingFileById(id);
            if (file == null) {
                log.warn("Onboarding file not found with id: {}", id);
            }
            return file;
        } catch (Exception e) {
            log.error("Failed to query onboarding file by id: {}", id, e);
            throw new DatabaseOperationException("Query onboarding file failed",e);
        }
    }

    /**
     * Query Onboarding File list
     *
     * @param hrOnboardingFile Onboarding File
     * @return Onboarding File list
     */
    @Override
    public List<HrOnboardingFile> selectHrOnboardingFileList(HrOnboardingFile hrOnboardingFile)
    {
        try {
            // 允许查询条件为null，返回全部数据
            return baseMapper.selectHrOnboardingFileList(hrOnboardingFile);
        } catch (Exception e) {
            log.error("Failed to query onboarding file list", e);
            throw new DatabaseOperationException("Query onboarding file list failed", e);
        }
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
        // 参数校验：实体不能为空
        if (hrOnboardingFile == null) {
            log.error("Insert onboarding file failed: entity is null");
            throw new IllegalArgumentException("Onboarding file entity cannot be null");
        }

        // 业务校验：必填字段检查（根据实际业务补充）
        if (hrOnboardingFile.getEmployeesId() == null || hrOnboardingFile.getEmployeesId().trim().isEmpty()) {
            log.error("Insert onboarding file failed: employeeId is null or empty");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        if (hrOnboardingFile.getFileName() == null || hrOnboardingFile.getFileName().trim().isEmpty()) {
            log.error("Insert onboarding file failed: fileName is null or empty");
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        try {
            hrOnboardingFile.setCreateTime(DateUtils.getNowDate());
            return baseMapper.insert(hrOnboardingFile);
        } catch (DuplicateKeyException e) {
            log.error("Insert onboarding file failed: duplicate key for file [{}]", hrOnboardingFile.getFileName(), e);
            throw new DuplicateKeyException("File already exists: " + hrOnboardingFile.getFileName());
        } catch (DataIntegrityViolationException e) {
            log.error("Insert onboarding file failed: data integrity violation for employee [{}]",
                    hrOnboardingFile.getEmployeesId(), e);
            throw new DataIntegrityViolationException("Invalid file data");
        } catch (Exception e) {
            log.error("Insert onboarding file failed", e);
            throw new ServiceException("Create onboarding file failed");
        }
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
        // 参数校验
        if (hrOnboardingFile == null) {
            log.error("Update onboarding file failed: entity is null");
            throw new IllegalArgumentException("Onboarding file entity cannot be null");
        }
        if (hrOnboardingFile.getId() == null || hrOnboardingFile.getId().trim().isEmpty()) {
            log.error("Update onboarding file failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding file ID cannot be null or empty");
        }

        // 业务校验：检查记录是否存在
        HrOnboardingFile existingFile = selectHrOnboardingFileById(hrOnboardingFile.getId());
        if (existingFile == null) {
            log.error("Update onboarding file failed: file not found with id [{}]", hrOnboardingFile.getId());
            throw new ServiceException("Onboarding file not found: " + hrOnboardingFile.getId());
        }

        try {
            log.info("Updating onboarding file: {}", hrOnboardingFile.getId());
            hrOnboardingFile.setUpdateTime(DateUtils.getNowDate());

            int result = baseMapper.updateById(hrOnboardingFile);
            if (result == 0) {
                log.warn("No onboarding file updated with id: {}", hrOnboardingFile.getId());
            }
            return result;
        } catch (DataIntegrityViolationException e) {
            log.error("Update onboarding file failed: data integrity violation for id [{}]",
                    hrOnboardingFile.getId(), e);
            throw new DataIntegrityViolationException("Invalid update data");
        } catch (Exception e) {
            log.error("Update onboarding file failed for id: {}", hrOnboardingFile.getId(), e);
            throw new ServiceException("Update onboarding file failed");
        }
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
        // 参数校验
        if (ids == null || ids.length == 0) {
            log.error("Batch delete onboarding file failed: ids is null or empty");
            throw new IllegalArgumentException("IDs cannot be null or empty");
        }

        try {
            // 检查是否存在无效ID（可选，根据业务需要）
            for (String id : ids) {
                if (id == null || id.trim().isEmpty()) {
                    log.error("Batch delete failed: contains null or empty id");
                    throw new IllegalArgumentException("ID in batch cannot be null or empty");
                }
            }

            return baseMapper.deleteBatchIds(Arrays.asList(ids));
        } catch (DataIntegrityViolationException e) {
            log.error("Batch delete onboarding file failed: data integrity violation for ids [{}]",
                    Arrays.toString(ids), e);
            throw new DataIntegrityViolationException("Cannot delete files with existing references");
        } catch (Exception e) {
            log.error("Batch delete onboarding file failed for ids: {}", Arrays.toString(ids), e);
            throw new ServiceException("Batch delete onboarding files failed: " + e.getMessage());
        }
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
        if (id == null || id.trim().isEmpty()) {
            log.error("Delete onboarding file failed: id is null or empty");
            throw new IllegalArgumentException("Onboarding file ID cannot be null or empty");
        }

        if (selectHrOnboardingFileById(id) == null) {
            log.error("Delete onboarding file failed: file not found with id [{}]", id);
            throw new ServiceException("Onboarding file not found: " + id);
        }

        try {
            return baseMapper.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            log.error("Delete onboarding file failed: data integrity violation for id [{}]", id, e);
            throw new DataIntegrityViolationException("Cannot delete file with existing references");
        } catch (Exception e) {
            log.error("Delete onboarding file failed for id: {}", id, e);
            throw new ServiceException("Delete onboarding file failed");
        }
    }

    @Override
    public List<HrOnboardingFile> selectHrOnboardingFileListByEmp(HrOnboardingFile hrOnboardingFile) {
        // 参数校验
        if (hrOnboardingFile == null || hrOnboardingFile.getEmployeesId() == null || hrOnboardingFile.getEmployeesId().trim().isEmpty()) {
            log.error("Query by employee failed: employeeId is null or empty");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        try {
            return baseMapper.selectHrOnboardingFileListByEmp(hrOnboardingFile);
        } catch (Exception e) {
            log.error("Failed to query onboarding file list by employee [{}]", hrOnboardingFile.getEmployeesId(), e);
            throw new DatabaseOperationException("Query files by employee failed", e);
        }
    }
}
