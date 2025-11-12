package com.ys.sync.domain;

import lombok.Data;

/**
 * @description:
 * @author: xz_Frank
 * @date: 2025/10/30
 */
@Data
public class Errors {
    private String messages;
    private String type;
    private String param;
}
