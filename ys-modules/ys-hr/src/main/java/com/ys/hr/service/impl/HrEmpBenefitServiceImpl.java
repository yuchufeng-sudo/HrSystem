package com.ys.hr.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.hr.domain.HrAttendance;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrEmpBenefitMapper;
import com.ys.hr.domain.HrEmpBenefit;
import com.ys.hr.service.IHrEmpBenefitService;
import com.ys.common.core.utils.DateUtils;

/**
 *   Employee Welfare Application Form Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrEmpBenefitServiceImpl extends ServiceImpl<HrEmpBenefitMapper, HrEmpBenefit> implements IHrEmpBenefitService
{

    /**
     * Query Employee Welfare Application Form
     *
     * @param benefitEmpId   Employee Welfare Application Form primary key
     * @return   Employee Welfare Application Form
     */
    @Override
    public HrEmpBenefit selectHrEmpBenefitByBenefitEmpId(String benefitEmpId)
    {
        return baseMapper.selectHrEmpBenefitByBenefitEmpId(benefitEmpId);
    }

    /**
     * Query Employee Welfare Application Form list
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return   Employee Welfare Application Form
     */
    @Override
    public List<HrEmpBenefit> selectHrEmpBenefitList(HrEmpBenefit hrEmpBenefit)
    {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<HrEmpBenefit> hrEmpBenefits = new ArrayList<>();
        if ("1".equals(hrEmpBenefit.getSearchValue())) {
            LocalDate sevenDaysAgo = today.minusDays(7);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else if ("2".equals(hrEmpBenefit.getSearchValue())) {
            // Data from 14 days ago
            LocalDate sevenDaysAgo = today.minusDays(14);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else if ("3".equals(hrEmpBenefit.getSearchValue())) {
            // Data from 7 days ago
            LocalDate sevenDaysAgo = today.minusDays(30);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else {
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        }
        return hrEmpBenefits;
    }

    /**
     * Add Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    @Override
    public int insertHrEmpBenefit(HrEmpBenefit hrEmpBenefit)
    {
        hrEmpBenefit.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(hrEmpBenefit);
    }

    /**
     * Update Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    @Override
    public int updateHrEmpBenefit(HrEmpBenefit hrEmpBenefit)
    {
        hrEmpBenefit.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(hrEmpBenefit);
    }

    /**
     * Batch delete   Employee Welfare Application Form
     *
     * @param benefitEmpIds   Employee Welfare Application Form primary keys to be deleted
     * @return Result
     */
    @Override
    public int deleteHrEmpBenefitByBenefitEmpIds(String[] benefitEmpIds)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(benefitEmpIds));
    }

    /**
     * Delete Employee Welfare Application Form information
     *
     * @param benefitEmpId   Employee Welfare Application Form primary key
     * @return Result
     */
    @Override
    public int deleteHrEmpBenefitByBenefitEmpId(String benefitEmpId)
    {
        return baseMapper.deleteById(benefitEmpId);
    }
}
