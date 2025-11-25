package com.ys.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.hr.domain.HrEmployeeContract;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Employee Contract Mapper Interface
 *
 * @author collection
 * @date 2023-02-26
 */
public interface HrEmployeeContractMapper extends BaseMapper<HrEmployeeContract>
{
    /**
     * Query Employee Contract   list
     *
     * @param employeeContract  Employee Contract
     * @return  Employee Contract Set
     */
    List<HrEmployeeContract> selectTbEmpContractList(HrEmployeeContract employeeContract);

    /**
     * Query existing contract list
     * @param employeeContract
     * @return
     */
    List<HrEmployeeContract> selectTbEmpContracts(HrEmployeeContract employeeContract);

    public Long selectUserCountByRid(@Param("uid") String uid,
                                     @Param("status") String status,
                                     @Param("rid") Long rid);

    /**
     * According to Contract idQUERY Information
     * @param contractNo
     * @return
     */
    public HrEmployeeContract selectByFileId(String contractNo);

    /**
     * Query Contract Information
     * @param employeeContract
     * @return
     */
    public HrEmployeeContract selectEmpContractInfo(HrEmployeeContract employeeContract);

    List<HrEmployeeContract> selectTbEmpContractList1(HrEmployeeContract employeeContract);

    /**
     * Query expired contracts.
     * @return
     */
    List<HrEmployeeContract> selectExpiringContracts();
}
