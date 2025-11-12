package com.ys.sync.domain;

import lombok.Data;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/30
 */
@Data
public class ErrorResponse {
    private String status;
    private Errors errors;
}
