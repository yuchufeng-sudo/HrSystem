package com.ys.hr.controller.api.v1;

import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.hr.domain.dto.EmployeeCreateDTO;
import com.ys.hr.domain.dto.EmployeeUpdateDTO;
import com.ys.hr.domain.dto.EmployeeResponseDTO;
import com.ys.hr.domain.dto.EmployeeListResponseDTO;
import com.ys.hr.service.IEmployeeApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * External Employee API Controller V1
 *
 * RESTful API for external integrations to manage employee lifecycle:
 * - Create new employees
 * - Update employee information
 * - Offboard (resign) employees
 * - Restore (reactivate) employees
 *
 * Authentication: Bearer token (API Key or JWT)
 * Base URL: /api/v1/employees
 *
 * @author ys
 * @version 1.0
 * @since 2025-11-28
 */
@Tag(name = "Employee Management API V1", description = "External API for employee lifecycle management")
@RestController
@RequestMapping("/api/v1/employees")
@Validated
public class EmployeeApiController {

    @Resource
    private IEmployeeApiService employeeApiService;

    /**
     * Get list of employees
     *
     * @param page Page number (default: 1)
     * @param size Page size (default: 20, max: 100)
     * @param status Employee status filter (ACTIVE, RESIGNED, ALL)
     * @return List of employees with pagination metadata
     */
    @Operation(summary = "Get employee list", description = "Retrieve paginated list of employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<EmployeeListResponseDTO> getEmployees(
            @Parameter(description = "Page number", example = "1")
            @RequestParam(defaultValue = "1") Integer page,

            @Parameter(description = "Page size (max: 100)", example = "20")
            @RequestParam(defaultValue = "20") Integer size,

            @Parameter(description = "Employee status (ACTIVE, RESIGNED, ALL)", example = "ACTIVE")
            @RequestParam(defaultValue = "ACTIVE") String status) {

        try {
            // Validate page size
            if (size > 100) {
                return ResponseEntity.badRequest()
                        .body(EmployeeListResponseDTO.error("Page size cannot exceed 100"));
            }

            EmployeeListResponseDTO response = employeeApiService.getEmployeeList(page, size, status);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(EmployeeListResponseDTO.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeListResponseDTO.error("Failed to retrieve employees: " + e.getMessage()));
        }
    }

    /**
     * Get employee details by ID
     *
     * @param employeeId Employee unique identifier
     * @return Employee details (without sensitive data like password)
     */
    @Operation(summary = "Get employee details", description = "Retrieve detailed information for a specific employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable @NotBlank(message = "Employee ID cannot be empty") String employeeId) {

        try {
            EmployeeResponseDTO response = employeeApiService.getEmployeeById(employeeId);

            if (response == null || response.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(EmployeeResponseDTO.error("Employee not found with ID: " + employeeId, 404));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeResponseDTO.error("Failed to retrieve employee: " + e.getMessage(), 500));
        }
    }

    /**
     * Create new employee
     *
     * @param employeeDTO Employee creation data
     * @return Created employee with full details including generated ID
     */
    @Operation(summary = "Create new employee", description = "Create a new employee record in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - validation errors"),
            @ApiResponse(responseCode = "409", description = "Conflict - employee with same email/phone already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Log(title = "Create Employee via API", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Parameter(description = "Employee data", required = true)
            @Valid @RequestBody EmployeeCreateDTO employeeDTO) {

        try {
            EmployeeResponseDTO response = employeeApiService.createEmployee(employeeDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(EmployeeResponseDTO.error("Validation error: " + e.getMessage(), 400));
        } catch (IllegalStateException e) {
            // Conflict - duplicate employee
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(EmployeeResponseDTO.error(e.getMessage(), 409));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeResponseDTO.error("Failed to create employee: " + e.getMessage(), 500));
        }
    }

    /**
     * Update employee information
     *
     * @param employeeId Employee unique identifier
     * @param employeeDTO Updated employee data
     * @return Updated employee with full current details
     */
    @Operation(summary = "Update employee",
            description = "Update employee information. Returns the complete updated employee record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - validation errors"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Log(title = "Update Employee via API", businessType = BusinessType.UPDATE)
    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable @NotBlank(message = "Employee ID cannot be empty") String employeeId,

            @Parameter(description = "Updated employee data", required = true)
            @Valid @RequestBody EmployeeUpdateDTO employeeDTO) {

        try {
            EmployeeResponseDTO response = employeeApiService.updateEmployee(employeeId, employeeDTO);

            if (response == null || response.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(EmployeeResponseDTO.error("Employee not found with ID: " + employeeId, 404));
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(EmployeeResponseDTO.error("Validation error: " + e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeResponseDTO.error("Failed to update employee: " + e.getMessage(), 500));
        }
    }

    /**
     * Offboard employee (resignation)
     *
     * RESTful approach: PUT /employees/{id}/offboard
     * This marks an employee as resigned and sets the exit date.
     *
     * @param employeeId Employee unique identifier
     * @param resignationDate Exit date (format: yyyy-MM-dd)
     * @param reason Resignation reason (optional)
     * @return Updated employee status with offboarding details
     */
    @Operation(summary = "Offboard employee",
            description = "Mark employee as resigned. Employee record is retained but status changed to RESIGNED.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee offboarded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - validation errors"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - employee already offboarded"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Log(title = "Offboard Employee via API", businessType = BusinessType.UPDATE)
    @PutMapping("/{employeeId}/offboard")
    public ResponseEntity<EmployeeResponseDTO> offboardEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable @NotBlank(message = "Employee ID cannot be empty") String employeeId,

            @Parameter(description = "Resignation date (yyyy-MM-dd)", example = "2025-12-31")
            @RequestParam(required = false) String resignationDate,

            @Parameter(description = "Resignation reason", example = "Career change")
            @RequestParam(required = false) @Size(max = 500, message = "Reason cannot exceed 500 characters")
            String reason) {

        try {
            EmployeeResponseDTO response = employeeApiService.offboardEmployee(employeeId, resignationDate, reason);

            if (response == null || response.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(EmployeeResponseDTO.error("Employee not found with ID: " + employeeId, 404));
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(EmployeeResponseDTO.error("Validation error: " + e.getMessage(), 400));
        } catch (IllegalStateException e) {
            // Conflict - employee already offboarded
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(EmployeeResponseDTO.error(e.getMessage(), 409));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeResponseDTO.error("Failed to offboard employee: " + e.getMessage(), 500));
        }
    }

    /**
     * Restore employee (reactivation after resignation)
     *
     * RESTful approach: PUT /employees/{id}/restore
     * This reactivates a previously resigned employee.
     *
     * @param employeeId Employee unique identifier
     * @param returnDate Return to work date (format: yyyy-MM-dd)
     * @return Updated employee status with restoration details
     */
    @Operation(summary = "Restore employee",
            description = "Reactivate a previously resigned employee. Changes status back to ACTIVE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee restored successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request - validation errors"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - employee is already active"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @Log(title = "Restore Employee via API", businessType = BusinessType.UPDATE)
    @PutMapping("/{employeeId}/restore")
    public ResponseEntity<EmployeeResponseDTO> restoreEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable @NotBlank(message = "Employee ID cannot be empty") String employeeId) {

        try {
            EmployeeResponseDTO response = employeeApiService.restoreEmployee(employeeId);

            if (response == null || response.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(EmployeeResponseDTO.error("Employee not found with ID: " + employeeId, 404));
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(EmployeeResponseDTO.error("Validation error: " + e.getMessage(), 400));
        } catch (IllegalStateException e) {
            // Conflict - employee already active
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(EmployeeResponseDTO.error(e.getMessage(), 409));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(EmployeeResponseDTO.error("Failed to restore employee: " + e.getMessage(), 500));
        }
    }
}
