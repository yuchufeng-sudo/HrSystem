package com.ys.utils.email;

import lombok.Data;

import java.util.Date;

@Data
public class MessageEntity {

    private String sender;

    private String senderName;

    private Date sendTime;

    private String recipient;

    private String emailSubject;

    private String emailCc;

    private String emailBcc;

    private String emailContent;

    private String messageId;

}
