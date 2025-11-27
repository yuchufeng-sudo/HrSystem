package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.DataAccessException;
import com.ys.common.core.exception.ResourceNotFoundException;
import com.ys.common.core.exception.ValidationException;
import com.ys.common.core.utils.DateUtils;
import com.ys.hr.domain.HrFundInformation;
import com.ys.hr.enums.FundType;
import com.ys.hr.mapper.HrFundInformationMapper;
import com.ys.hr.service.IHrFundInformationService;
import com.ys.hr.validator.FundInformationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Fund Information Service Implementation (Optimized Version)
 *
 * @author ys
 * @date 2025-10-17
 */
@Slf4j
@Service
public class HrFundInformationServiceImpl
        extends ServiceImpl<HrFundInformationMapper, HrFundInformation>
        implements IHrFundInformationService {

    @Autowired
    private FundInformationValidator validator;

    /**
     * Query fund information list
     */
    @Override
    public List<HrFundInformation> selectHrFundInformationList(HrFundInformation hrFundInformation) {
        try {
            log.debug("Querying fund information with criteria: {}", hrFundInformation);
            return baseMapper.selectHrFundInformationList(hrFundInformation);

        } catch (Exception e) {
            log.error("Database error while querying fund information list", e);
            throw new DataAccessException("query fund information list",
                    "Failed to retrieve fund information from database", e);
        }
    }

    /**
     * Find fund information by employee ID and enterprise ID
     */
    @Override
    public Optional<HrFundInformation> findByEmployeeAndEnterprise(
            String employeeId, String enterpriseId) {

        if (!StringUtils.hasText(employeeId) || !StringUtils.hasText(enterpriseId)) {
            throw new ValidationException("Employee ID and Enterprise ID are required");
        }

        try {
            log.debug("Finding fund information for employee: {}, enterprise: {}",
                    employeeId, enterpriseId);

            LambdaQueryWrapper<HrFundInformation> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(HrFundInformation::getEmployeeId, employeeId)
                    .eq(HrFundInformation::getEnterpriseId, enterpriseId)
                    .last("LIMIT 1");

            HrFundInformation result = baseMapper.selectOne(wrapper);
            return Optional.ofNullable(result);

        } catch (Exception e) {
            log.error("Error finding fund information for employee: {}", employeeId, e);
            throw new DataAccessException("find fund information",
                    "Failed to query fund information", e);
        }
    }

    /**
     * Check if record exists
     */
    @Override
    public boolean exists(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }

        try {
            return baseMapper.selectById(id) != null;
        } catch (Exception e) {
            log.error("Error checking existence of fund information: {}", id, e);
            throw new DataAccessException("check existence",
                    "Failed to verify record existence", e);
        }
    }

    /**
     * Insert fund information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertHrFundInformation(HrFundInformation hrFundInformation) {

        log.info("Inserting new fund information for employee: {}",
                hrFundInformation.getEmployeeId());

        try {
            // Validate fund type
            validator.validateFundType(hrFundInformation.getFundType());

            // Execute specific validation based on type
            if (FundType.APRA.getCode().equals(hrFundInformation.getFundType())) {
                validator.validateApraFund(hrFundInformation);
            } else if (FundType.SMSF.getCode().equals(hrFundInformation.getFundType())) {
                validator.validateSmsfFund(hrFundInformation);
            }

            // Set creation time
            hrFundInformation.setCreateTime(DateUtils.getNowDate());

            // Execute insert
            int rows = baseMapper.insert(hrFundInformation);

            if (rows == 0) {
                throw new DataAccessException("insert fund information",
                        "No rows were inserted");
            }

            log.info("Successfully inserted fund information with ID: {}",
                    hrFundInformation.getId());

        } catch (ValidationException e) {
            log.warn("Validation failed during insert: {}", e.getMessage());
            throw e;

        } catch (DuplicateKeyException e) {
            log.error("Duplicate fund information for employee: {}",
                    hrFundInformation.getEmployeeId(), e);
            throw new ValidationException(
                    "A fund information record already exists for this employee");

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during insert", e);
            throw new ValidationException(
                    "Data integrity constraint violated. Please check your input.");

        } catch (Exception e) {
            log.error("Unexpected error during insert", e);
            throw new DataAccessException("insert fund information",
                    "Failed to create fund information record", e);
        }
    }

    /**
     * Update fund information
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHrFundInformation(HrFundInformation hrFundInformation) {

        log.info("Updating fund information: {}", hrFundInformation.getId());

        try {
            // Verify record exists
            if (!exists(hrFundInformation.getId())) {
                throw new ResourceNotFoundException("Fund Information",
                        hrFundInformation.getId());
            }

            // Validate fund type
            validator.validateFundType(hrFundInformation.getFundType());

            // Clear unused fields based on type
            clearUnusedFieldsByType(hrFundInformation);

            // Execute specific validation based on type
            if (FundType.APRA.getCode().equals(hrFundInformation.getFundType())) {
                validator.validateApraFund(hrFundInformation);
            } else if (FundType.SMSF.getCode().equals(hrFundInformation.getFundType())) {
                validator.validateSmsfFund(hrFundInformation);
            }

            // Set update time
            hrFundInformation.setUpdateTime(DateUtils.getNowDate());

            // Execute update
            int rows = baseMapper.updateById(hrFundInformation);

            if (rows == 0) {
                log.warn("No rows updated for fund information: {}", hrFundInformation.getId());
                throw new DataAccessException("update fund information",
                        "No rows were updated. Record may have been modified or deleted.");
            }

            log.info("Successfully updated fund information: {}", hrFundInformation.getId());
            return rows;

        } catch (ResourceNotFoundException | ValidationException e) {
            log.warn("Error during update: {}", e.getMessage());
            throw e;

        } catch (DataIntegrityViolationException e) {
            log.error("Data integrity violation during update", e);
            throw new ValidationException(
                    "Data integrity constraint violated. Please check your input.");

        } catch (Exception e) {
            log.error("Unexpected error during update", e);
            throw new DataAccessException("update fund information",
                    "Failed to update fund information record", e);
        }
    }

    /**
     * Clear unused fields based on fund type
     */
    private void clearUnusedFieldsByType(HrFundInformation fundInfo) {
        String fundType = fundInfo.getFundType();

        if (FundType.APRA.getCode().equals(fundType)) {
            // APRA fund, clear SMSF-related fields
            clearSmsfFields(fundInfo);
            log.debug("Cleared SMSF fields for APRA fund");

        } else if (FundType.SMSF.getCode().equals(fundType)) {
            // SMSF fund, clear APRA-related fields
            clearApraFields(fundInfo);
            log.debug("Cleared APRA fields for SMSF fund");
        }
    }

    /**
     * Clear SMSF-related fields
     */
    private void clearSmsfFields(HrFundInformation fundInfo) {
        fundInfo.setEsa(null);
        fundInfo.setSmsfName(null);
        fundInfo.setSmsfAbn(null);
        fundInfo.setSmsfAccountName(null);
        fundInfo.setSmsfMemberNumber(null);
        fundInfo.setFundContactEmail(null);
        fundInfo.setBankAccountBsb(null);
        fundInfo.setBankAccountName(null);
        fundInfo.setBankAccountNumber(null);
    }

    /**
     * Clear APRA-related fields
     */
    private void clearApraFields(HrFundInformation fundInfo) {
        fundInfo.setFundId(null);
        fundInfo.setApraAccountName(null);
        fundInfo.setMemberAccountNumber(null);
    }

    /**
     * Batch delete fund information
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByIds(String[] ids) {

        if (ids == null || ids.length == 0) {
            throw new ValidationException("IDs array cannot be empty");
        }

        log.info("Deleting {} fund information records", ids.length);

        try {
            int deletedCount = baseMapper.deleteBatchIds(Arrays.asList(ids));

            if (deletedCount != ids.length) {
                log.warn("Expected to delete {} records but deleted {}",
                        ids.length, deletedCount);
            }

            return deletedCount;

        } catch (Exception e) {
            log.error("Error deleting fund information records", e);
            throw new DataAccessException("delete fund information",
                    "Failed to delete records", e);
        }
    }
}
