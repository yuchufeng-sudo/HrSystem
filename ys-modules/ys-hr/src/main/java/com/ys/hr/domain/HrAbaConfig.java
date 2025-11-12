package com.ys.hr.domain;

import lombok.Data;

@Data
public class HrAbaConfig {
    public static final int SENDER_NAME_MAX_LENGTH = 32;
    public static final int ACCOUNT_NUM_MAX_LENGTH = 9;
    public static final int BSB_MAX_LENGTH = 7; // BSB length including hyphen (6 digits + 1 hyphen)
    public static final int BANK_CODE_MAX_LENGTH = 3;
    public static final int ACPA_NUM_MAX_LENGTH = 6;
    public static final int DESCRIPTION_MAX_LENGTH = 12;
    public static final int RECIPIENT_NAME_MAX_LENGTH = 32;
    public static final int REFERENCE_MAX_LENGTH = 18;
    public static final int AMOUNT_MAX_LENGTH = 10;
}