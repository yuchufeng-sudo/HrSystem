package com.ys.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.hr.domain.HrEmpBenefit;

import java.util.List;

/**
 *   Employee Welfare Application Form Service Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface IHrEmpBenefitService extends IService<HrEmpBenefit>
{
    /**
     * Query Employee Welfare Application Form
     *
     * @param benefitEmpId   Employee Welfare Application Form primary key
     * @return   Employee Welfare Application Form
     */
    public HrEmpBenefit selectHrEmpBenefitByBenefitEmpId(String benefitEmpId);

    /**
     * Query Employee Welfare Application Form list
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return   Employee Welfare Application Form collection
     */
    public List<HrEmpBenefit> selectHrEmpBenefitList(HrEmpBenefit hrEmpBenefit);

    /**
     * Add Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    public int insertHrEmpBenefit(HrEmpBenefit hrEmpBenefit);

    /**
     * Update Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    public int updateHrEmpBenefit(HrEmpBenefit hrEmpBenefit);

    /**
     * Batch delete   Employee Welfare Application Form
     *
     * @param benefitEmpIds   Employee Welfare Application Form primary keys to be deleted
     * @return Result
     */
    public int deleteHrEmpBenefitByBenefitEmpIds(String[] benefitEmpIds);

    /**
     * Delete Employee Welfare Application Form information
     *
     * @param benefitEmpId   Employee Welfare Application Form primary key
     * @return Result
     */
    public int deleteHrEmpBenefitByBenefitEmpId(String benefitEmpId);
}
