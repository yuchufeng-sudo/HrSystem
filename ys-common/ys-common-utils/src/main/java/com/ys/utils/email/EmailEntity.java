package com.ys.utils.email;

import lombok.Data;

@Data
public class EmailEntity {
    private String FromEmail;
    private String toEmail;
    private String title;
    private String content;
}
