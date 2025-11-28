package com.ys.hr.service;

import com.ys.hr.domain.dto.EmployeeCreateDTO;
import com.ys.hr.domain.dto.EmployeeUpdateDTO;
import com.ys.hr.domain.dto.EmployeeResponseDTO;
import com.ys.hr.domain.dto.EmployeeListResponseDTO;

/**
 * Employee API Service Interface
 *
 * Business logic for external employee API operations
 * Handles validation, transformation, and orchestration
 *
 * @author ys
 * @version 1.0
 */
public interface IEmployeeApiService {

    /**
     * Get paginated list of employees
     *
     * @param page Page number (starts from 1)
     * @param size Page size (max 100)
     * @param status Filter by status (ACTIVE, RESIGNED, ALL)
     * @return Paginated employee list with metadata
     * @throws IllegalArgumentException if parameters are invalid
     */
    EmployeeListResponseDTO getEmployeeList(Integer page, Integer size, String status);

    /**
     * Get employee details by ID
     *
     * @param employeeId Employee unique identifier
     * @return Employee details without sensitive data
     * @return null if employee not found
     */
    EmployeeResponseDTO getEmployeeById(String employeeId);

    /**
     * Create new employee
     *
     * @param employeeDTO Employee creation data
     * @return Created employee with generated ID and full details
     * @throws IllegalArgumentException if validation fails
     * @throws IllegalStateException if employee with same email/phone exists
     */
    EmployeeResponseDTO createEmployee(EmployeeCreateDTO employeeDTO);

    /**
     * Update existing employee
     *
     * Supports partial updates - only provided fields will be updated
     *
     * @param employeeId Employee unique identifier
     * @param employeeDTO Updated employee data
     * @return Updated employee with complete current details
     * @return null if employee not found
     * @throws IllegalArgumentException if validation fails
     */
    EmployeeResponseDTO updateEmployee(String employeeId, EmployeeUpdateDTO employeeDTO);

    /**
     * Offboard employee (mark as resigned)
     *
     * @param employeeId Employee unique identifier
     * @param resignationDate Resignation date (yyyy-MM-dd format), uses today if null
     * @param reason Resignation reason (optional)
     * @return Updated employee status
     * @return null if employee not found
     * @throws IllegalArgumentException if validation fails (invalid date, etc.)
     * @throws IllegalStateException if employee already resigned
     */
    EmployeeResponseDTO offboardEmployee(String employeeId, String resignationDate, String reason);

    /**
     * Restore employee (reactivate after resignation)
     *
     * @param employeeId Employee unique identifier
     * @return Updated employee status
     * @return null if employee not found
     * @throws IllegalArgumentException if validation fails
     * @throws IllegalStateException if employee already active
     */
    EmployeeResponseDTO restoreEmployee(String employeeId);
}
