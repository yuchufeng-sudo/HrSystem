package com.ys.hr.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Employee Creation DTO
 * Used for creating new employees via external API
 *
 * Design principles applied:
 * - No sensitive data like passwords
 * - Consistent field naming (camelCase, no abbreviations)
 * - Clear validation rules
 * - Comprehensive documentation
 *
 * @author ys
 * @version 1.0
 */
@Data
@Schema(description = "Employee creation request data")
public class EmployeeCreateDTO {

    // Basic Information
    @Schema(description = "First name", required = true, example = "John")
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Schema(description = "Last name", required = true, example = "Doe")
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Schema(description = "Email address", required = true, example = "john.doe@company.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Schema(description = "Date of birth", required = true, example = "1990-05-15")
    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Schema(description = "Gender (Man, Woman, Non-Binary)", example = "MALE")
    @Pattern(regexp = "^(Man|Woman|Non-Binary)$",
            message = "Gender must be Man, Woman, or Non-Binary")
    private String gender;

    // Contact Information
    @Schema(description = "Country phone code", required = true, example = "+1")
    @NotBlank(message = "Phone code is required")
    @Pattern(regexp = "^\\+\\d{1,4}$", message = "Invalid phone code format (e.g., +1, +44, +86)")
    private String phoneCode;

    @Schema(description = "Contact phone number", required = true, example = "5551234567")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\d{7,15}$", message = "Phone number must be 7-15 digits")
    private String contactPhone;

    @Schema(description = "Emergency contact name", example = "Jane Doe")
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;

    @Schema(description = "Emergency contact phone", example = "+15559876543")
    @Pattern(regexp = "^\\+[1-9]\\d{0,2}\\d{4,14}$", message = "Invalid emergency contact format")
    private String emergencyContactPhone;

    // Address Information
    @Schema(description = "Country", required = true, example = "United States")
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Schema(description = "Address Line 1", example = "123 Main Street")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address1;

    @Schema(description = "Address Line 2", example = "Apt 4B")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address2;

    @Schema(description = "City", required = true, example = "New York")
    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Schema(description = "State/Province", example = "NY")
    @Size(max = 100, message = "State/Province cannot exceed 100 characters")
    private String province;

    @Schema(description = "Postal code", required = true, example = "10001")
    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code cannot exceed 20 characters")
    private String postalCode;

    // Employment Information
    @Schema(description = "Employment type (1=Full-time, 2=Part-time, 3=Internship)",
            example = "1")
    @Pattern(regexp = "^[123]$",
            message = "Invalid employment type (must be 1, 2, or 3)")
    private String employmentType;

    @Schema(description = "Payroll ID", example = "PAY123456")
    @Size(max = 50, message = "Payroll ID cannot exceed 50 characters")
    private String payrollId;

    @Schema(description = "Pay rate (annual salary or hourly rate)", example = "75000.00")
    @DecimalMin(value = "0.0", message = "Pay rate must be positive")
    private Double payRate;

    @Schema(description = "Pay frequency (Daily, Weekly, Every 2 Weeks, Twice a month, Monthly)",
            example = "Weekly")
    @Pattern(regexp = "^(Daily|Weekly|Every 2 Weeks|Twice a month|Monthly)$",
            message = "Invalid pay frequency")
    private String payFrequency;

    @Schema(description = "Work location/office", example = "New York Office")
    @Size(max = 100, message = "Work location cannot exceed 100 characters")
    private String workLocation;
}
