package com.ys.hr.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Employee List Response DTO
 * Used for returning paginated list of employees
 *
 * @author ys
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Paginated employee list response")
public class EmployeeListResponseDTO {

    @Schema(description = "Success status of the operation", example = "true")
    private Boolean success;

    @Schema(description = "Error message (only present if success=false)", example = "Invalid page number")
    private String errorMessage;

    @Schema(description = "HTTP status code", example = "200")
    private Integer statusCode;

    @Schema(description = "List of employees")
    private List<EmployeeResponseDTO.EmployeeData> data;

    @Schema(description = "Pagination metadata")
    private PaginationMetadata pagination;

    /**
     * Pagination metadata
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaginationMetadata {

        @Schema(description = "Current page number", example = "1")
        private Integer currentPage;

        @Schema(description = "Number of items per page", example = "20")
        private Integer pageSize;

        @Schema(description = "Total number of items", example = "157")
        private Long totalItems;

        @Schema(description = "Total number of pages", example = "8")
        private Integer totalPages;

        @Schema(description = "Whether there is a next page", example = "true")
        private Boolean hasNextPage;

        @Schema(description = "Whether there is a previous page", example = "false")
        private Boolean hasPreviousPage;
    }

    /**
     * Create a success response with employee list and pagination
     */
    public static EmployeeListResponseDTO success(
            List<EmployeeResponseDTO.EmployeeData> data,
            int currentPage,
            int pageSize,
            long totalItems) {

        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        PaginationMetadata pagination = PaginationMetadata.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .hasNextPage(currentPage < totalPages)
                .hasPreviousPage(currentPage > 1)
                .build();

        return EmployeeListResponseDTO.builder()
                .success(true)
                .statusCode(200)
                .data(data)
                .pagination(pagination)
                .build();
    }

    /**
     * Create an error response
     */
    public static EmployeeListResponseDTO error(String errorMessage) {
        return EmployeeListResponseDTO.builder()
                .success(false)
                .statusCode(400)
                .errorMessage(errorMessage)
                .build();
    }

    /**
     * Create an error response with custom status code
     */
    public static EmployeeListResponseDTO error(String errorMessage, int statusCode) {
        return EmployeeListResponseDTO.builder()
                .success(false)
                .statusCode(statusCode)
                .errorMessage(errorMessage)
                .build();
    }
}
