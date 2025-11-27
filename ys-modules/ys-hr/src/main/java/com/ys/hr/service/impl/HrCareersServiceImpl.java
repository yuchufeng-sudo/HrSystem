package com.ys.hr.service.impl;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.model.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ys.hr.mapper.HrCareersMapper;
import com.ys.hr.domain.HrCareers;
import com.ys.hr.service.IHrCareersService;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.exception.DataAccessException;
import com.ys.common.core.exception.ResourceNotFoundException;
import com.ys.common.core.exception.ValidationException;
import org.springframework.dao.DuplicateKeyException;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.sql.SQLException;

/**
 * Company careers information Service Implementation
 *
 * @author ys
 * @date 2025-09-27
 */
@Slf4j
@Service
public class HrCareersServiceImpl extends ServiceImpl<HrCareersMapper, HrCareers> implements IHrCareersService
{

    /**
     * Query Company careers information
     *
     * @param id Company careers information primary key
     * @return Company careers information
     */
    @Override
    public HrCareers selectHrCareersById(String id)
    {
        if (!StringUtils.hasText(id)) {
            throw new ValidationException("Career ID is required for query");
        }

        try {
            log.debug("Querying career information with ID: {}", id);
            HrCareers result = baseMapper.selectHrCareersById(id);

            if (result == null) {
                log.debug("Career information not found with ID: {}", id);
                throw new ResourceNotFoundException("Career Information", id);
            }
            return result;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Database error while querying career information with ID: {}", id, e);
            throw new DataAccessException("query career information",
                    "Failed to retrieve career information from database", e);
        }
    }

    /**
     * Query Company careers information list
     *
     * @param hrCareers Company careers information
     * @return Company careers information
     */
    @Override
    public List<HrCareers> selectHrCareersList(HrCareers hrCareers)
    {
        try {
            log.debug("Querying career information list with criteria: {}", hrCareers);
            return baseMapper.selectHrCareersList(hrCareers);
        } catch (Exception e) {
            log.error("Database error while querying career information list", e);
            throw new DataAccessException("query career information list",
                    "Failed to retrieve career information list from database", e);
        }
    }

    /**
     * Add Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertHrCareers(HrCareers hrCareers)
    {
        if (hrCareers == null) {
            throw new ValidationException("Career information cannot be null");
        }

        if (!StringUtils.hasText(hrCareers.getEnterpriseId())) {
            throw new ValidationException("Enterprise ID is required for career creation");
        }

        try {
            log.info("Inserting new career information for enterprise: {}", hrCareers.getEnterpriseId());
            hrCareers.setCreateTime(DateUtils.getNowDate());
            int rows = baseMapper.insert(hrCareers);

            if (rows == 0) {
                throw new DataAccessException("insert career information", "No rows were inserted");
            }

            log.info("Successfully inserted career information with ID: {}", hrCareers.getId());
            return rows;
        } catch (DuplicateKeyException e) {
            log.error("Duplicate career information for enterprise: {}", hrCareers.getEnterpriseId(), e);
            throw new ValidationException("A career information record already exists for this enterprise");
        } catch (DataAccessException e) {
            log.error("Data access error during career information insert", e);
            throw new ValidationException("Data constraint violated. Please check your input data.");
        } catch (Exception e) {
            log.error("Unexpected error during career information insert", e);
            throw new DataAccessException("insert career information",
                    "Failed to create career information record", e);
        }
    }

    /**
     * Update Company careers information
     *
     * @param hrCareers Company careers information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateHrCareers(HrCareers hrCareers)
    {
        if (hrCareers == null || !StringUtils.hasText(hrCareers.getId())) {
            throw new ValidationException("Career ID is required for update");
        }

        try {
            log.info("Updating career information with ID: {}", hrCareers.getId());

            // Verify record exists
            selectHrCareersById(hrCareers.getId());

            hrCareers.setUpdateTime(DateUtils.getNowDate());
            int rows = baseMapper.updateById(hrCareers);

            if (rows == 0) {
                log.warn("No rows updated for career information: {}", hrCareers.getId());
                throw new DataAccessException("update career information",
                        "No rows were updated. Record may have been modified or deleted.");
            }

            log.info("Successfully updated career information: {}", hrCareers.getId());
            return rows;
        } catch (ResourceNotFoundException e) {
            log.warn("Update failed: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Data access error during career information update", e);
            throw new ValidationException("Data constraint violated. Please check your input data.");
        } catch (Exception e) {
            log.error("Unexpected error during career information update", e);
            throw new DataAccessException("update career information",
                    "Failed to update career information record", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrCareers getOrCreateEnterpriseCareer(String enterpriseId) {
        if (!StringUtils.hasText(enterpriseId)) {
            throw new ValidationException("Enterprise ID is required");
        }

        try {
            log.debug("Checking for existing career information for enterprise: {}", enterpriseId);
            HrCareers query = new HrCareers();
            query.setEnterpriseId(enterpriseId);
            List<HrCareers> existing = selectHrCareersList(query);

            if (!existing.isEmpty()) {
                HrCareers result = existing.get(0);
                log.debug("Found existing career information for enterprise: {}", enterpriseId);
                return result;
            }

            log.info("No existing career found for enterprise: {}. Creating new one.", enterpriseId);
            LoginUser user = SecurityUtils.getLoginUser();

            if (user == null) {
                throw new ValidationException("No logged-in user found");
            }

            HrCareers newCareer = HrCareers.builder()
                    .enterpriseId(enterpriseId)
                    .careersName(user.getEnterpriseName())
                    .logo(user.getEnterpriseLogo())
                    .build();

            insertHrCareers(newCareer);
            return newCareer;
        } catch (ValidationException e) {
            log.warn("Validation failed in getOrCreate: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error in getOrCreateEnterpriseCareer for enterprise: {}", enterpriseId, e);
            throw new DataAccessException("get or create career information",
                    "Failed to retrieve or create career information", e);
        }
    }
}
