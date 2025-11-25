package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmpBenefit;

import java.util.List;

/**
 *   Employee Welfare Application Form Mapper Interface
 *
 * @author ys
 * @date 2025-06-09
 */
public interface HrEmpBenefitMapper extends BaseMapper<HrEmpBenefit>
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

}
