package com.ys.hr.enums;

/**
 * Candidate Status Enum
 *
 * Defines all possible states for a candidate throughout the recruitment process.
 * Replaces magic strings with type-safe enum values.
 *
 * @author ys
 * @date 2025-11-26
 */
public enum CandidateStatus {

    /**
     * Screening status
     */
    SCREENING("0", "Screening"),

    /**
     * Shortlisted status
     */
    SHORTLISTED("1", "Shortlisted"),

    /**
     * Interview status
     */
    INTERVIEW("2", "Interview"),

    /**
     * Hired status
     */
    HIRED("3", "Hired"),

    /**
     * Rejected status
     */
    REJECTED("4", "Rejected");

    /**
     * Status code stored in database
     */
    private final String code;

    /**
     * Human-readable status description
     */
    private final String description;

    /**
     * Constructor for CandidateStatus enum
     *
     * @param code Status code for database storage
     * @param description Human-readable description
     */
    CandidateStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Get the status code
     *
     * @return Status code string
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the status description
     *
     * @return Status description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Find CandidateStatus by code
     *
     * @param code Status code to search for
     * @return Matching CandidateStatus, or null if not found
     */
    public static CandidateStatus fromCode(String code) {
        for (CandidateStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * Check if matches the specified status code
     *
     * @param code Status code
     * @return Whether it matches
     */
    public boolean equalsCode(String code) {
        return this.code.equals(code);
    }
}
