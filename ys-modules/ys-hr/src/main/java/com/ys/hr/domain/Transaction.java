package com.ys.hr.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    // 6-digit BSB (without hyphen)
    private String bsb;
    // Account number (up to 9 digits)
    private String accountNumber;
    // Beneficiary name (up to 32 characters)
    private String beneficiaryName;
    // Amount (AUD)
    private BigDecimal amount;
    // Transaction code (default 53 - ordinary credit)
    private String transactionCode = "53";
    // Lodgement reference (18 characters)
    private String lodgementReference;
}
