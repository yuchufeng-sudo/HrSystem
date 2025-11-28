package com.ys.hr.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Employee Response DTO
 * Used for returning employee data via external API
 *
 * Security considerations:
 * - NEVER includes password or any authentication credentials
 * - Uses consistent field naming (createdBy, createdDate, not createBy/createDate)
 * - No magic numbers - all codes are documented
 * - No duplicated fields (no fullName when firstName/lastName exist)
 *
 * @author ys
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Employee response data")
public class EmployeeResponseDTO {

    @Schema(description = "Success status of the operation", example = "true")
    private Boolean success;

    @Schema(description = "Error message (only present if success=false)", example = "Employee not found")
    private String errorMessage;

    @Schema(description = "HTTP status code", example = "200")
    private Integer statusCode;

    @Schema(description = "Employee data (null if error occurred)")
    private EmployeeData data;

    /**
     * Employee data nested object
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmployeeData {

        // Identifier
        @Schema(description = "Unique employee identifier", example = "6575123456")
        private Long employeeId;

        // Basic Information
        @Schema(description = "First name", example = "John")
        private String firstName;

        @Schema(description = "Last name", example = "Doe")
        private String lastName;

        @Schema(description = "Email address", example = "john.doe@company.com")
        private String email;

        @Schema(description = "Date of birth", example = "1990-05-15")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date dateOfBirth;

        @Schema(description = "Gender (Man, Woman, Non-Binary)", example = "MALE")
        private String gender;

        // Contact Information
        @Schema(description = "Country phone code", example = "+1")
        private String phoneCode;

        @Schema(description = "Contact phone number", example = "5551234567")
        private String contactPhone;

        @Schema(description = "Emergency contact name", example = "Jane Doe")
        private String emergencyContactName;

        @Schema(description = "Emergency contact phone", example = "+15559876543")
        private String emergencyContactPhone;

        // Address Information
        @Schema(description = "Country", example = "United States")
        private String country;

        @Schema(description = "Address Line 1", example = "123 Main Street")
        private String address1;

        @Schema(description = "Address Line 2", example = "Apt 4B")
        private String address2;

        @Schema(description = "City", example = "New York")
        private String city;

        @Schema(description = "State/Province", example = "NY")
        private String province;

        @Schema(description = "Postal code", example = "10001")
        private String postalCode;

        // Employment Information
        @Schema(description = "Employment type (1=Active, 2=Resigned, 3=Offboarding)", example = "1")
        private String status;

        @Schema(description = "Employment type (1=Full-time, 2=Part-time, 3=Internship)", example = "1")
        private String employmentType;

        @Schema(description = "Job title", example = "Software Engineer")
        private String jobTitle;

        @Schema(description = "Department", example = "Engineering")
        private String department;

        @Schema(description = "Payroll ID", example = "PAY123456")
        private String payrollId;

        @Schema(description = "Pay rate (annual salary or hourly rate)", example = "75000.00")
        private Double payRate;

        @Schema(description = "Pay frequency (Daily, Weekly, Every 2 Weeks, Twice a month, Monthly)",
                example = "Weekly")
        private String payFrequency;

        @Schema(description = "Work location/office", example = "New York Office")
        private String workLocation;

        // Metadata
        @Schema(description = "Record creation timestamp", example = "2025-01-01T10:30:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date createdDate;

        @Schema(description = "User who created the record", example = "admin")
        private String createdBy;

        @Schema(description = "Last update timestamp", example = "2025-11-28T15:45:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date updatedDate;

        @Schema(description = "User who last updated the record", example = "hr_manager")
        private String updatedBy;
    }

    /**
     * Create a success response with employee data
     */
    public static EmployeeResponseDTO success(EmployeeData data) {
        return EmployeeResponseDTO.builder()
                .success(true)
                .statusCode(200)
                .data(data)
                .build();
    }

    /**
     * Create a success response with custom status code
     */
    public static EmployeeResponseDTO success(EmployeeData data, int statusCode) {
        return EmployeeResponseDTO.builder()
                .success(true)
                .statusCode(statusCode)
                .data(data)
                .build();
    }

    /**
     * Create an error response
     */
    public static EmployeeResponseDTO error(String errorMessage) {
        return EmployeeResponseDTO.builder()
                .success(false)
                .statusCode(400)
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * Create an error response with custom status code
     */
    public static EmployeeResponseDTO error(String errorMessage, int statusCode) {
        return EmployeeResponseDTO.builder()
                .success(false)
                .statusCode(statusCode)
                .errorMessage(errorMessage)
                .build();
    }
}
