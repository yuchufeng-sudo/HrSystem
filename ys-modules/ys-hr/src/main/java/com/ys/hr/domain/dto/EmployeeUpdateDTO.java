package com.ys.hr.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * Employee Update DTO
 * Used for updating existing employees via external API
 *
 * All fields are optional to support partial updates
 * Only provided fields will be updated
 *
 * @author ys
 * @version 1.0
 */
@Data
@Schema(description = "Employee update request data (all fields optional for partial updates)")
public class EmployeeUpdateDTO {

    // Basic Information
    @Schema(description = "First name", example = "John")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@company.com")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Schema(description = "Date of birth", example = "1990-05-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Schema(description = "Gender (Man, Woman, Non-Binary)", example = "MALE")
    @Pattern(regexp = "^(Man|Woman|Non-Binary)$",
            message = "Gender must be Man, Woman, or Non-Binary")
    private String gender;

    // Contact Information
    @Schema(description = "Country phone code", example = "+1")
    @Pattern(regexp = "^\\+\\d{1,4}$", message = "Invalid phone code format (e.g., +1, +44, +86)")
    private String phoneCode;

    @Schema(description = "Contact phone number", example = "5551234567")
    @Pattern(regexp = "^\\d{7,15}$", message = "Phone number must be 7-15 digits")
    private String contactPhone;

    @Schema(description = "Emergency contact name", example = "Jane Doe")
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;

    @Schema(description = "Emergency contact phone", example = "+15559876543")
    @Pattern(regexp = "^\\+[1-9]\\d{0,2}\\d{4,14}$", message = "Invalid emergency contact format")
    private String emergencyContactPhone;

    // Address Information
    @Schema(description = "Country", example = "United States")
    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Schema(description = "Address Line 1", example = "123 Main Street")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address1;

    @Schema(description = "Address Line 2", example = "Apt 4B")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address2;

    @Schema(description = "City", example = "New York")
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

    @Schema(description = "Pay rate (annual salary or hourly rate)", example = "85000.00")
    @DecimalMin(value = "0.0", message = "Pay rate must be positive")
    private Double payRate;

    @Schema(description = "Pay frequency (Daily, Weekly, Every 2 Weeks, Twice a month, Monthly)",
            example = "Weekly")
    @Pattern(regexp = "^(Daily|Weekly|Every 2 Weeks|Twice a month|Monthly)$",
            message = "Invalid pay frequency")
    private String payFrequency;

    @Schema(description = "Work location/office", example = "San Francisco Office")
    @Size(max = 100, message = "Work location cannot exceed 100 characters")
    private String workLocation;
}
