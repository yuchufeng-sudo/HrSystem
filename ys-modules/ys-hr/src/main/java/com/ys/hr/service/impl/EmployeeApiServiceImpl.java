package com.ys.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.dto.*;
import com.ys.hr.enums.EmployeeStatus;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IEmployeeApiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Employee API Service Implementation
 *
 * Business logic for external employee API operations
 *
 * @author ys
 * @version 1.0
 */
@Service
public class EmployeeApiServiceImpl implements IEmployeeApiService {

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public EmployeeListResponseDTO getEmployeeList(Integer page, Integer size, String status) {
        try {
            // 验证参数
            if (page < 1) {
                throw new IllegalArgumentException("Page number must be at least 1");
            }
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("Page size must be between 1 and 100");
            }

            // 构建查询条件
            LambdaQueryWrapper<HrEmployees> queryWrapper = new LambdaQueryWrapper<>();

            // 多租户过滤 - 只查询当前企业的员工
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.hasText(enterpriseId)) {
                queryWrapper.eq(HrEmployees::getEnterpriseId, enterpriseId);
            }

            // 状态过滤
            if (!"ALL".equalsIgnoreCase(status)) {
                queryWrapper.eq(HrEmployees::getStatus, status);
            }

            // 按创建时间降序
            queryWrapper.orderByDesc(HrEmployees::getCreateTime);

            // 分页查询
            Page<HrEmployees> pageQuery = new Page<>(page, size);
            Page<HrEmployees> pageResult = hrEmployeesMapper.selectPage(pageQuery, queryWrapper);

            // 转换为DTO
            List<EmployeeResponseDTO.EmployeeData> employeeDataList = pageResult.getRecords().stream()
                    .map(this::convertToEmployeeData)
                    .collect(Collectors.toList());

            // 返回分页结果
            return EmployeeListResponseDTO.success(
                    employeeDataList,
                    page,
                    size,
                    pageResult.getTotal()
            );

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve employee list: " + e.getMessage());
        }
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(String employeeId) {
        try {
            HrEmployees employee = hrEmployeesMapper.selectById(employeeId);

            if (employee == null) {
                return null;
            }

            // 多租户检查
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.hasText(enterpriseId) && !enterpriseId.equals(employee.getEnterpriseId())) {
                // 不属于当前企业
                return null;
            }

            EmployeeResponseDTO.EmployeeData data = convertToEmployeeData(employee);
            return EmployeeResponseDTO.success(data);

        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponseDTO createEmployee(EmployeeCreateDTO dto) {
        try {
            // 检查邮箱是否已存在
            LambdaQueryWrapper<HrEmployees> emailQuery = new LambdaQueryWrapper<>();
            emailQuery.eq(HrEmployees::getEmail, dto.getEmail());
            if (hrEmployeesMapper.selectCount(emailQuery) > 0) {
                throw new IllegalStateException("Employee with email " + dto.getEmail() + " already exists");
            }

            // 检查手机号是否已存在
            if (StringUtils.hasText(dto.getContactPhone())) {
                LambdaQueryWrapper<HrEmployees> phoneQuery = new LambdaQueryWrapper<>();
                phoneQuery.eq(HrEmployees::getContactPhone, dto.getContactPhone());
                if (hrEmployeesMapper.selectCount(phoneQuery) > 0) {
                    throw new IllegalStateException("Employee with phone " + dto.getContactPhone() + " already exists");
                }
            }

            // 创建员工实体
            HrEmployees employee = new HrEmployees();

            // 基本信息
            employee.setFullName(dto.getFirstName()+" "+dto.getLastName());
            employee.setFirstName(dto.getFirstName());
            employee.setLastName(dto.getLastName());
            employee.setEmail(dto.getEmail());
            employee.setDateOfBirth(dto.getDateOfBirth());
            employee.setGender(dto.getGender());

            // 联系信息
            employee.setPhoneCode(dto.getPhoneCode());
            employee.setContactPhone(dto.getContactPhone());
            employee.setEmergencyContactName(dto.getEmergencyContactName());
            employee.setEmergencyContact(dto.getEmergencyContactPhone());

            // 地址信息
            employee.setCountry(dto.getCountry());
            employee.setAddress1(dto.getAddress1());
            employee.setAddress2(dto.getAddress2());
            employee.setCity(dto.getCity());
            employee.setProvince(dto.getProvince());
            employee.setPostCode(dto.getPostalCode());

            // 雇佣信息
            employee.setEmploymentType(dto.getEmploymentType());
            employee.setPayrollId(dto.getPayrollId());
            employee.setPayRate(dto.getPayRate() != null ? dto.getPayRate().toString() : null);
            employee.setPayFrequency(dto.getPayFrequency());
            employee.setWorksAt(dto.getWorkLocation());

            // 状态
            employee.setStatus("ACTIVE");

            // 多租户
            employee.setEnterpriseId(SecurityUtils.getUserEnterpriseId());

            // 审计信息
            employee.setCreateBy(SecurityUtils.getUsername());
            employee.setCreateTime(DateUtils.getNowDate());
            employee.setUpdateBy(SecurityUtils.getUsername());
            employee.setUpdateTime(DateUtils.getNowDate());

            // 插入数据库
            hrEmployeesMapper.insert(employee);

            // 返回创建的员工信息
            EmployeeResponseDTO.EmployeeData data = convertToEmployeeData(employee);
            return EmployeeResponseDTO.success(data, 201);

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to create employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponseDTO updateEmployee(String employeeId, EmployeeUpdateDTO dto) {
        try {
            // 查询员工
            HrEmployees employee = hrEmployeesMapper.selectById(employeeId);
            if (employee == null) {
                return null;
            }

            // 多租户检查
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.hasText(enterpriseId) && !enterpriseId.equals(employee.getEnterpriseId())) {
                return null;
            }

            // 部分更新 - 只更新非null字段
            if (StringUtils.hasText(dto.getFirstName())) {
                employee.setFirstName(dto.getFirstName());
            }
            if (StringUtils.hasText(dto.getLastName())) {
                employee.setLastName(dto.getLastName());
            }
            if (StringUtils.hasText(dto.getEmail())) {
                employee.setEmail(dto.getEmail());
            }
            if (dto.getDateOfBirth() != null) {
                employee.setDateOfBirth(dto.getDateOfBirth());
            }
            if (StringUtils.hasText(dto.getGender())) {
                employee.setGender(dto.getGender());
            }
            if (StringUtils.hasText(dto.getPhoneCode())) {
                employee.setPhoneCode(dto.getPhoneCode());
            }
            if (StringUtils.hasText(dto.getContactPhone())) {
                employee.setContactPhone(dto.getContactPhone());
            }
            if (StringUtils.hasText(dto.getEmergencyContactName())) {
                employee.setEmergencyContactName(dto.getEmergencyContactName());
            }
            if (StringUtils.hasText(dto.getEmergencyContactPhone())) {
                employee.setEmergencyContact(dto.getEmergencyContactPhone());
            }
            if (StringUtils.hasText(dto.getCountry())) {
                employee.setCountry(dto.getCountry());
            }
            if (StringUtils.hasText(dto.getAddress1())) {
                employee.setStreetAddress(dto.getAddress1());
            }
            if (StringUtils.hasText(dto.getAddress2())) {
                employee.setStreetAddress(dto.getAddress2());
            }
            if (StringUtils.hasText(dto.getCity())) {
                employee.setCity(dto.getCity());
            }
            if (StringUtils.hasText(dto.getProvince())) {
                employee.setProvince(dto.getProvince());
            }
            if (StringUtils.hasText(dto.getPostalCode())) {
                employee.setPostCode(dto.getPostalCode());
            }
            if (StringUtils.hasText(dto.getEmploymentType())) {
                employee.setEmploymentType(dto.getEmploymentType());
            }
            if (StringUtils.hasText(dto.getPayrollId())) {
                employee.setPayrollId(dto.getPayrollId());
            }
            if (dto.getPayRate() != null) {
                employee.setPayRate(dto.getPayRate().toString());
            }
            if (StringUtils.hasText(dto.getPayFrequency())) {
                employee.setPayFrequency(dto.getPayFrequency());
            }
            if (StringUtils.hasText(dto.getWorkLocation())) {
                employee.setWorksAt(dto.getWorkLocation());
            }

            // 更新审计信息
            employee.setUpdateBy(SecurityUtils.getUsername());
            employee.setUpdateTime(DateUtils.getNowDate());

            // 更新数据库
            hrEmployeesMapper.updateById(employee);

            // 返回更新后的完整员工信息
            EmployeeResponseDTO.EmployeeData data = convertToEmployeeData(employee);
            return EmployeeResponseDTO.success(data);

        } catch (Exception e) {
            throw new ServiceException("Failed to update employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponseDTO offboardEmployee(String employeeId, String resignationDate, String reason) {
        try {
            // 查询员工
            HrEmployees employee = hrEmployeesMapper.selectById(employeeId);
            if (employee == null) {
                return null;
            }

            // 多租户检查
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.hasText(enterpriseId) && !enterpriseId.equals(employee.getEnterpriseId())) {
                return null;
            }

            // 检查当前状态
            if ("RESIGNED".equalsIgnoreCase(employee.getStatus())) {
                throw new IllegalStateException("Employee " + employeeId + " is already resigned");
            }

            // 解析退出日期
            Date exitDate;
            if (StringUtils.hasText(resignationDate)) {
                try {
                    exitDate = SIMPLE_DATE_FORMAT.parse(resignationDate);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid exit date format. Use yyyy-MM-dd");
                }
            } else {
                exitDate = DateUtils.getNowDate();
            }

            // 更新状态
            employee.setStatus(EmployeeStatus.RESIGNED.getCode());
            employee.setResignationDate(exitDate);
            employee.setResignationReason(reason);

            // 更新审计信息
            employee.setUpdateBy(SecurityUtils.getUsername());
            employee.setUpdateTime(DateUtils.getNowDate());

            // 更新数据库
            hrEmployeesMapper.updateById(employee);

            // 返回更新后的员工信息
            EmployeeResponseDTO.EmployeeData data = convertToEmployeeData(employee);
            return EmployeeResponseDTO.success(data);

        } catch (IllegalStateException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to offboard employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployeeResponseDTO restoreEmployee(String employeeId) {
        try {
            // 查询员工
            HrEmployees employee = hrEmployeesMapper.selectById(employeeId);
            if (employee == null) {
                return null;
            }

            // 多租户检查
            String enterpriseId = SecurityUtils.getUserEnterpriseId();
            if (StringUtils.hasText(enterpriseId) && !enterpriseId.equals(employee.getEnterpriseId())) {
                return null;
            }

            // 检查当前状态
            if ("ACTIVE".equalsIgnoreCase(employee.getStatus())) {
                throw new IllegalStateException("Employee " + employeeId + " is already active");
            }

            // 更新状态
            employee.setStatus("ACTIVE");
            // 清除退出日期
            employee.setResignationDate(null);

            // 更新审计信息
            employee.setUpdateBy(SecurityUtils.getUsername());
            employee.setUpdateTime(DateUtils.getNowDate());

            // 更新数据库
            hrEmployeesMapper.updateById(employee);

            // 返回更新后的员工信息
            EmployeeResponseDTO.EmployeeData data = convertToEmployeeData(employee);
            return EmployeeResponseDTO.success(data);

        } catch (IllegalStateException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to restore employee: " + e.getMessage());
        }
    }

    /**
     * 转换员工实体为DTO
     *
     * 重要: 不包含密码等敏感信息
     */
    private EmployeeResponseDTO.EmployeeData convertToEmployeeData(HrEmployees employee) {
        if (employee == null) {
            return null;
        }

        return EmployeeResponseDTO.EmployeeData.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .phoneCode(employee.getPhoneCode())
                .contactPhone(employee.getContactPhone())
                .emergencyContactName(employee.getEmergencyContactName())
                .emergencyContactPhone(employee.getEmergencyContact())
                .country(employee.getCountry())
                .address1(employee.getAddress1())
                .address2(employee.getAddress2())
                .city(employee.getCity())
                .province(employee.getProvince())
                .postalCode(employee.getPostCode())
                .status(employee.getStatus())
                .employmentType(employee.getEmploymentType())
                .jobTitle(employee.getPositionName())
                .department(employee.getDeptName())
                .payrollId(employee.getPayrollId())
                .payRate(employee.getPayRate() != null ? Double.parseDouble(employee.getPayRate()) : null)
                .payFrequency(employee.getPayFrequency())
                .workLocation(employee.getWorksAt())
                .createdDate(employee.getCreateTime())
                .createdBy(employee.getCreateBy())
                .updatedDate(employee.getUpdateTime())
                .updatedBy(employee.getUpdateBy())
                .build();
    }
}
