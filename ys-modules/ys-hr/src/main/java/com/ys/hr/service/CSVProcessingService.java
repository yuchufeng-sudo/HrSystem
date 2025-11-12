package com.ys.hr.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ys.hr.domain.HrAbaConfig;
import com.ys.hr.exception.ABAValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class CSVProcessingService {

    private static final int ABA_LINE_LENGTH = 120;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyy");

    public String convertCsvToAba(InputStream inputStream, String senderName,
            String senderAccount, String senderBsb,
            String senderBank, String acpaNumber,
            boolean strictMode, String batchDescription)
            throws IOException, CsvValidationException {

        validateSenderInfo(senderName, senderAccount, senderBsb, senderBank, acpaNumber, strictMode);

        StringBuilder abaContent = new StringBuilder();
        BigDecimal totalCredit = BigDecimal.ZERO;
        int recordCount = 0;
        String processingDate = LocalDate.now().format(DATE_FORMATTER);

        // 1. File header record (type 0)
        abaContent
                .append(createFileHeader(senderName, senderBsb, senderBank, acpaNumber, processingDate,
                        batchDescription))
                .append(System.lineSeparator());

        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            String[] header = reader.readNext();
            validateHeader(header);

            String[] nextLine;
            int rowNumber = 1;
            while ((nextLine = reader.readNext()) != null) {
                rowNumber++;
                if (nextLine.length < 5) {
                    if (strictMode) {
                        throw new ABAValidationException(
                                "Row " + rowNumber + " has insufficient data fields. At least 5 fields are required.");
                    }
                    continue;
                }

                String bsb = nextLine[0].trim();
                String accountNum = nextLine[1].trim();
                String name = nextLine[2].trim();
                BigDecimal amount = parseAmount(nextLine[3].trim(), rowNumber, strictMode);
                String reference = nextLine[4].trim();

                // Validate and format fields
                bsb = validateBsb(bsb, rowNumber, strictMode);
                accountNum = validateAccount(accountNum, rowNumber, strictMode);
                name = validateName(name, rowNumber, strictMode);
                reference = validateReference(reference, rowNumber, strictMode);

                // 2. Transaction record (type 1)
                abaContent.append(createDetailRecord(
                        bsb, accountNum, name, amount, reference,
                        senderBsb, senderAccount, senderName)).append(System.lineSeparator());

                totalCredit = totalCredit.add(amount);
                recordCount++;
            }
        }

        // 3. File footer record (type 7)
        abaContent.append(createFileFooter(totalCredit, recordCount))
                .append(System.lineSeparator());

        return abaContent.toString();
    }

    private String createFileHeader(String senderName, String senderBsb,
            String senderBank, String acpaNumber,
            String processingDate, String description) {
        // 0 01[Bank Code][Sender Name ][User ID][Description][Date]
        String bankCode = mapBankCode(senderBank);
        String formattedBsb = formatBsbForHeader(senderBsb); // New BSB formatting method

        return String.format(
                "0 %-17s01%-3s%-26s%s%-12s%-6s%-40s", // Modified format string
                "",
                bankCode,
                truncate(senderName, 26),
                formattedBsb, // Use formatted BSB
                truncate(description != null ? description : "PAYMENT", 12),
                processingDate,
                "");
    }

    // New BSB formatting method (for file header only)
    private String formatBsbForHeader(String bsb) {
        if (bsb == null)
            return "000-000";
        // Ensure BSB is in xxx-xxx format
        if (bsb.matches("\\d{3}-\\d{3}")) {
            return bsb;
        }
        // Try to format non-standard BSB
        String cleanBsb = bsb.replaceAll("[^0-9]", "");
        if (cleanBsb.length() == 6) {
            return cleanBsb.substring(0, 3) + "-" + cleanBsb.substring(3);
        }
        return "000-000";
    }

    private String createDetailRecord(String bsb, String accountNum, String name,
            BigDecimal amount, String reference,
            String senderBsb, String senderAccount,
            String senderName) {
        // 1[BSB7][Account9][Indicator][Transaction
        // Code][Amount10][Name32][Reference18][Sender BSB7][Sender Account9][Sender
        // Name16][Withholding Tax8]
        long cents = amount.multiply(BigDecimal.valueOf(100)).longValue();

        return String.format(
                "1%-7s%-9s %-2s%010d%-32s%-18s%-7s%-9s%-16s%08d",
                formatBsbForDetail(bsb), // Use formatted BSB
                truncate(accountNum, 9),
                "50", // Transaction code (50=Credit payment)
                cents,
                truncate(name, 32),
                truncate(reference, 18),
                formatBsbForDetail(senderBsb), // Format sender BSB
                truncate(senderAccount, 9),
                truncate(senderName, 16),
                0 // Withholding tax
        );
    }

    // New BSB formatting method (for transaction records only)
    private String formatBsbForDetail(String bsb) {
        if (bsb == null)
            return "000-000";
        // Ensure BSB is in xxx-xxx format
        if (bsb.matches("\\d{3}-\\d{3}")) {
            return bsb;
        }
        // Try to format non-standard BSB
        String cleanBsb = bsb.replaceAll("[^0-9]", "");
        if (cleanBsb.length() >= 6) {
            return cleanBsb.substring(0, 3) + "-" + cleanBsb.substring(3, 6);
        }
        // Pad with zeros if less than 6 digits
        return String.format("%06d", Integer.parseInt(cleanBsb))
                .replaceFirst("(\\d{3})(\\d{3})", "$1-$2");
    }

    private String createFileFooter(BigDecimal totalCredit, int recordCount) {
        long totalCents = totalCredit.multiply(BigDecimal.valueOf(100)).longValue();
        return String.format(
                "7%-7s%-12s%010d%010d%010d%-24s%06d%-40s",
                "999-999", // Use hyphenated BSB
                "",
                0L, // Net amount
                totalCents, // Total credits
                0L, // Total debits
                "",
                recordCount,
                "");
    }

    // Validation methods
    private void validateSenderInfo(String senderName, String senderAccount, String senderBsb,
            String senderBank, String acpaNumber, boolean strictMode) {
        checkFieldLength("Sender name", senderName, HrAbaConfig.SENDER_NAME_MAX_LENGTH, strictMode);
        checkFieldLength("Sender account", senderAccount, HrAbaConfig.ACCOUNT_NUM_MAX_LENGTH, strictMode);
        checkFieldLength("Sender BSB", senderBsb, HrAbaConfig.BSB_MAX_LENGTH, strictMode);
        checkFieldLength("Sender bank", senderBank, HrAbaConfig.BANK_CODE_MAX_LENGTH, strictMode);
        checkFieldLength("ACPA number", acpaNumber, HrAbaConfig.ACPA_NUM_MAX_LENGTH, strictMode);

        // Validate sender BSB format
        validateBsbFormat(senderBsb, "Sender BSB");
    }

    private void checkFieldLength(String fieldName, String value, int maxLength, boolean strictMode) {
        if (value == null) {
            if (strictMode)
                throw new ABAValidationException(fieldName + "Cannot be empty.");
            return;
        }

        if (value.length() > maxLength) {
            if (strictMode) {
                throw new ABAValidationException(
                        String.format("%s exceeds the length limit: %d characters allowed, but %d characters provided",
                                fieldName, maxLength, value.length()));
            }
        }
    }

    private void validateHeader(String[] header) {
        List<String> expected = Arrays.asList(
                "bsb", "account_num", "name", "amount", "txn_reference");

        if (header == null || header.length < expected.size()) {
            throw new ABAValidationException("The CSV header is incomplete.");
        }

        for (int i = 0; i < expected.size(); i++) {
            if (!header[i].equalsIgnoreCase(expected.get(i))) {
                throw new ABAValidationException("Invalid CSV header, expected: " + expected + "，actual: " + Arrays.toString(header));
            }
        }
    }

    private String validateBsb(String bsb, int rowNumber, boolean strictMode) {
        if (bsb == null || bsb.isEmpty()) {
            if (strictMode)
                throw new ABAValidationException("Row " + rowNumber + " Row BSB cannot be empty.");
            return "000-000";
        }

       // Validate and format BSB
String formattedBsb = formatBsbForDetail(bsb);

// Additional validation in strict mode
        if (strictMode && !formattedBsb.matches("\\d{3}-\\d{3}")) {
            throw new ABAValidationException("Row " + rowNumber + " The BSB format of the row is invalid.: " + bsb);
        }

        return formattedBsb;
    }

    private void validateBsbFormat(String bsb, String fieldName) {
        if (bsb != null && !bsb.matches("\\d{3}-\\d{3}")) {
            throw new ABAValidationException(fieldName + " Invalid format.: " + bsb + " (Should be in the format of xxx-xxx)");
        }
    }

    private String validateAccount(String account, int rowNumber, boolean strictMode) {
        if (account == null || account.isEmpty()) {
            if (strictMode)
                throw new ABAValidationException("Row " + rowNumber + " The row account number cannot be empty.");
            return "00000000";
        }

        // Remove non-numeric characters
        String cleanAccount = account.replaceAll("[^0-9]", "");
        if (cleanAccount.length() > HrAbaConfig.ACCOUNT_NUM_MAX_LENGTH) {
            return cleanAccount.substring(0, HrAbaConfig.ACCOUNT_NUM_MAX_LENGTH);
        }
        return String.format("%9s", cleanAccount).replace(' ', '0');
    }

    private String validateName(String name, int rowNumber, boolean strictMode) {
        if (name == null || name.isEmpty()) {
            if (strictMode)
                throw new ABAValidationException("Row " + rowNumber + " The row name cannot be empty.");
            return "Unknown";
        }

        // Remove characters that are not letters, spaces, and hyphens.
        String cleanName = name.replaceAll("[^a-zA-Z\\s-]", "");
        if (cleanName.length() > HrAbaConfig.RECIPIENT_NAME_MAX_LENGTH) {
            return cleanName.substring(0, HrAbaConfig.RECIPIENT_NAME_MAX_LENGTH);
        }
        return cleanName;
    }

    private String validateReference(String reference, int rowNumber, boolean strictMode) {
        if (reference == null)
            reference = "";

        if (reference.length() > HrAbaConfig.REFERENCE_MAX_LENGTH) {
            return reference.substring(0, HrAbaConfig.REFERENCE_MAX_LENGTH);
        }
        return reference;
    }

    private BigDecimal parseAmount(String amountStr, int rowNumber, boolean strictMode) {
        if (amountStr == null || amountStr.isEmpty()) {
            if (strictMode)
                throw new ABAValidationException("The amount in row " + rowNumber + " cannot be empty.");
            return BigDecimal.ZERO;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ABAValidationException("The amount in row " + rowNumber + " must be greater than 0: " + amountStr);
            }
            return amount;
        } catch (NumberFormatException e) {
            if (strictMode)
                throw new ABAValidationException("The amount format in row " + rowNumber + " is invalid: " + amountStr);
            return BigDecimal.ZERO;
        }
    }

    private String mapBankCode(String bankName) {
        String upperBankName = bankName.toUpperCase();
        switch (upperBankName) {
            case "COMMERZBANK":
                return "CBA";
            case "ANZ":
                return "ANZ";
            case "NATIONAL BANK OF AUSTRALIA":
                return "NAB";
            case "WESTPAC":
                return "WBC";
            default:
                return upperBankName;
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null)
            return "";
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }
}