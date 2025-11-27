package com.ys.hr.validator;

import com.ys.common.core.exception.ValidationException;
import com.ys.hr.domain.HrFundInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Fund information validator
 */
@Slf4j
@Component
public class FundInformationValidator {

    // ABN format: 11 digits
    private static final Pattern ABN_PATTERN = Pattern.compile("^\\d{11}$");

    // USI format: 20 characters (letters and numbers)
    private static final Pattern USI_PATTERN = Pattern.compile("^[A-Z0-9]{20}$");

    // BSB format: 6 digits, may have hyphen in middle
    private static final Pattern BSB_PATTERN = Pattern.compile("^\\d{3}-?\\d{3}$");

    // Account format: 6-10 digits
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^\\d{6,10}$");

    /**
     * Validate APRA fund information
     */
    public void validateApraFund(HrFundInformation fundInfo) {
        log.debug("Validating APRA fund information");

        // Validate Fund ID (USI)
        if (!StringUtils.hasText(fundInfo.getFundId())) {
            throw new ValidationException("fundId", "Fund ID is required for APRA fund");
        }

        if (!USI_PATTERN.matcher(fundInfo.getFundId()).matches()) {
            throw new ValidationException("fundId",
                    "Fund ID must be 20 characters (letters and numbers)");
        }

        // Validate account name
        if (!StringUtils.hasText(fundInfo.getApraAccountName())) {
            throw new ValidationException("apraAccountName",
                    "Account name is required for APRA fund");
        }

        // Validate member account number
        if (!StringUtils.hasText(fundInfo.getMemberAccountNumber())) {
            throw new ValidationException("memberAccountNumber",
                    "Member account number is required for APRA fund");
        }
    }

    /**
     * Validate SMSF fund information
     */
    public void validateSmsfFund(HrFundInformation fundInfo) {
        log.debug("Validating SMSF fund information");

        // Validate fund name
        if (!StringUtils.hasText(fundInfo.getSmsfName())) {
            throw new ValidationException("smsfName", "SMSF name is required");
        }

        // Validate ABN
        if (!StringUtils.hasText(fundInfo.getSmsfAbn())) {
            throw new ValidationException("smsfAbn", "SMSF ABN is required");
        }

        if (!ABN_PATTERN.matcher(fundInfo.getSmsfAbn().replaceAll("\\s", "")).matches()) {
            throw new ValidationException("smsfAbn", "ABN must be 11 digits");
        }

        // Validate bank details
        validateBankDetails(fundInfo);
    }

    /**
     * Validate bank details
     */
    private void validateBankDetails(HrFundInformation fundInfo) {
        // Validate account name
        if (!StringUtils.hasText(fundInfo.getBankAccountName())) {
            throw new ValidationException("bankAccountName",
                    "Bank account name is required for SMSF");
        }

        // Validate BSB
        if (!StringUtils.hasText(fundInfo.getBankAccountBsb())) {
            throw new ValidationException("bankAccountBsb",
                    "BSB is required for SMSF");
        }

        String bsb = fundInfo.getBankAccountBsb().replaceAll("\\s", "");
        if (!BSB_PATTERN.matcher(bsb).matches()) {
            throw new ValidationException("bankAccountBsb",
                    "BSB must be 6 digits (format: XXX-XXX or XXXXXX)");
        }

        // Validate account number
        if (!StringUtils.hasText(fundInfo.getBankAccountNumber())) {
            throw new ValidationException("bankAccountNumber",
                    "Bank account number is required for SMSF");
        }

        String accountNumber = fundInfo.getBankAccountNumber().replaceAll("\\s", "");
        if (!ACCOUNT_PATTERN.matcher(accountNumber).matches()) {
            throw new ValidationException("bankAccountNumber",
                    "Bank account number must be 6-10 digits");
        }
    }

    /**
     * Validate fund type
     */
    public void validateFundType(String fundType) {
        if (!StringUtils.hasText(fundType)) {
            throw new ValidationException("fundType", "Fund type is required");
        }

        if (!"1".equals(fundType) && !"2".equals(fundType)) {
            throw new ValidationException("fundType",
                    "Fund type must be '1' (APRA) or '2' (SMSF)");
        }
    }
}
