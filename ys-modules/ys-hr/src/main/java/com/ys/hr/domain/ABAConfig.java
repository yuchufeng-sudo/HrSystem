package com.ys.hr.domain;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ABA file configuration class
 * @author Hzz
 */
@Data
public class ABAConfig {
    // Account name (26 characters)
    private String accountName;
    // APCA number (6 characters)
    private String apcaNumber;
    // Bank numeric code (01, 03, 08, 48)
    private String bankCode;
    // Processing date (DDMMYY format)
    private String processingDate;
    // Remitter name (16 characters)
    // private String remitter;
    // Remitter reference number (18 characters)
    private String remitterReference;
    // File description (default PAYROLL)
    private String fileDescription = "PAYROLL";
    // Financial institution abbreviation (BQL/WBC, etc.)
    private String bankAbbreviation;
    // Payer BSB (6-digit number)
    private String remitterBSB;
    // Payer account number
    private String remitterAccount;
    // Payer name
    private String remitterName;

    /**
     * Create ABAConfig from ABAFileRequest
     */
    public static ABAConfig fromRequest(ABAFileRequest request) {
        ABAConfig config = new ABAConfig();
        config.setAccountName(request.getAccoutName());
        config.setApcaNumber(request.getApca());
        // config.setBankCode(convertBankCode(request.getBankCode()));
        config.setProcessingDate(convertDateToABAFormat(request.getDate()));
        // config.setRemitter(request.getAccoutName());
        config.setRemitterReference(request.getAccoutNumber());
        config.setFileDescription(request.getDefaultText());
        config.setBankAbbreviation(resolveBankAbbreviation(request.getBankCode()));
        config.setRemitterBSB(request.getBsb().replace("-", ""));
        config.setRemitterAccount(request.getAccoutNumber());
        config.setRemitterName(request.getAccoutName());
        return config;
    }

    /**
     * Convert bank name to numeric code
     */
    private static String resolveBankAbbreviation(String bankCode) {
        String upperBankCode = bankCode.toUpperCase();
        switch (bankCode.toUpperCase()) {
            case "BQL":
                return "BQL";
            case "WBC":
                return "WBC";
            case "CBA":
                return "CBA";
            case "NAB":
                return "NAB";
            default:
                return upperBankCode; // Default
        }
    }

    /**
     * Convert date format (YYYY-MM-DD → DDMMYY)
     */
    private static String convertDateToABAFormat(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return localDate.format(DateTimeFormatter.ofPattern("ddMMyy"));
        } catch (Exception e) {
            // Use current date as fallback
            return LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy"));
        }
    }
}
