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
     * Candidate application submitted
     */
    APPLIED("1", "Applied"),

    /**
     * Candidate under review
     */
    UNDER_REVIEW("2", "Under Review"),

    /**
     * Candidate shortlisted for interview
     */
    SHORTLISTED("3", "Shortlisted"),

    /**
     * Candidate interviewed
     */
    INTERVIEWED("4", "Interviewed"),

    /**
     * Candidate hired
     */
    HIRED("5", "Hired"),

    /**
     * Candidate rejected
     */
    REJECTED("6", "Rejected");

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
}
