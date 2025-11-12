package com.ys.hr.sign.entiry;

import lombok.AllArgsConstructor;

/**
 * Platform Type
 */
@AllArgsConstructor
public enum SignType {

    /**
     * PandaDoc Electronic Signature 
     */
    PANDA_DOC(1);

    private final int code;


    public static SignType fromCode(int code) {
        for (SignType typeEnum : values()) {
            if (typeEnum.code == code) {
                return typeEnum;
            }
        }
        return null;
    }

    public int code() {
        return this.code;
    }

}
