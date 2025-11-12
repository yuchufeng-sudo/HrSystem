package com.ys.utils.email;

import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class EmailPullUtils {

    private static final String HOST = "imap.qq.com";

    private static final String PORT = "993";

    /**
     * Pull all emails
     * @param email
     * @param password Authorization Code
     * @return
     */
    public static List<MessageEntity> pullEmail(String email, String password){
        return fetchMessages(email, password, null, null, null);
    }

    /**
     * Pull the specified time
     * @param email
     * @param password
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<MessageEntity> pullEmail(String email, String password, String startTime, String endTime){
        return fetchMessages(email, password, null, startTime, endTime);
    }

    /**
     * Pull all emails of a specified contact
     * @param email
     * @param targetEmail
     * @param password
     * @return
     */
    public static List<MessageEntity> pullEmail(String email, String password, String targetEmail){
        return fetchMessages(email, password, targetEmail, null, null);
    }

    /**
     * Pull emails from a specified contact at a specified time
     * @param email
     * @param targetEmail
     * @param password
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<MessageEntity> pullEmail(String email, String password, String targetEmail, String startTime, String endTime){
        return fetchMessages(email, password, targetEmail, startTime, endTime);
    }

    /**
     * Construct search conditions and pull emails
     */
    private static List<MessageEntity> fetchMessages(String email, String password, String targetEmail, String startTime, String endTime) {
        List<MessageEntity> results = new ArrayList<>();
        Store store = null;
        Folder inbox = null;
        try {
            Properties props = new Properties();
            props.put("mail.imap.ssl.enable", "true");
            props.put("mail.imap.port", PORT);
            props.put("mail.imap.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props);
            store = session.getStore("imap");

            log.info("Connecting to a Mailbox Server: {}:{}, Email:{}", HOST, PORT, email);

            store.connect(HOST, email, password);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            log.info("The INBOX folder has been opened and mail retrieval has started...");

            // Constructing search criteria
            SearchTerm searchTerm = buildSearchTerm(targetEmail, startTime, endTime);

            Message[] messages = (searchTerm == null)
                    ? inbox.getMessages()
                    : inbox.search(searchTerm);

            log.info("Email:{}, {} emails retrieved", email, messages.length);

            for (Message msg : messages) {
                MessageEntity entity = new MessageEntity();
                entity.setEmailSubject(msg.getSubject());
                // 收件人
                Address[] froms = msg.getFrom();
                if (froms != null && froms.length > 0) {
                    // Get Mail
                    String senderEmail = ((InternetAddress) froms[0]).getAddress();
                    entity.setSender(senderEmail);
                    // Get Sender Name
                    String senderName  = ((InternetAddress) froms[0]).getPersonal();
                    entity.setSenderName(senderName);
                }
                entity.setSendTime(msg.getReceivedDate());
                entity.setEmailContent(getTextFromMessage(msg));
                // Recipient
                entity.setRecipient(extractEmails(msg.getRecipients(Message.RecipientType.TO)));
                // Cc
                entity.setEmailCc(extractEmails(msg.getRecipients(Message.RecipientType.CC)));
                // Blind sender
                entity.setEmailBcc(extractEmails(msg.getRecipients(Message.RecipientType.BCC)));
                String[] messageIdHeader = msg.getHeader("Message-ID");
                if (messageIdHeader != null && messageIdHeader.length > 0) {
                    String messageId = messageIdHeader[0];
                    entity.setMessageId(messageId);
                }
                results.add(entity);
                log.debug("Email: {}, Pull Email Details: subject={}, from={}, date={}",
                       email, entity.getEmailSubject(), entity.getSender(), entity.getSendTime());
            }

        } catch (AuthenticationFailedException e) {
            log.error("Login fail: Account is abnormal, service is not open, password is incorrect: {}", e.getMessage());
            throw new RuntimeException("Login fail: Account is abnormal, service is not open, password is incorrect");
        } catch (Exception e) {
            log.error("Email retrieval error, please try again: {}", e.getMessage());
            throw new RuntimeException("Email retrieval error, please try again");
        }
        finally {
            try {
                if (inbox != null && inbox.isOpen()) {
                    inbox.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (Exception ignore) {
            }
        }
        return results;
    }

    /**
     * Constructing search criteria
     */
    private static SearchTerm buildSearchTerm(String targetEmail, String startTime, String endTime) throws Exception {
        List<SearchTerm> terms = new ArrayList<>();

        // Sender matching (matching email addresses only)
        if (targetEmail != null && !targetEmail.isEmpty()) {
            SearchTerm senderTerm = new SearchTerm() {
                @Override
                public boolean match(Message msg) {
                    try {
                        Address[] froms = msg.getFrom();
                        if (froms != null) {
                            for (Address addr : froms) {
                                String emailAddr = ((InternetAddress) addr).getAddress();
                                if (emailAddr != null && emailAddr.equalsIgnoreCase(targetEmail)) {
                                    return true;
                                }
                            }
                        }
                    } catch (MessagingException e) {
                        log.error("TargetEmail: {}, Failed to match sender: {}", targetEmail, e.getMessage());
                    }
                    return false;
                }
            };
            terms.add(senderTerm);
            log.debug("Target Email:{}", targetEmail);
        }

        // By time interval
        if (startTime != null && endTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date startDate = sdf.parse(startTime);

            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(endTime));
            cal.add(Calendar.DAY_OF_MONTH, 1); // End date + 1 day, make sure to include the current day
            Date endDate = cal.getTime();

            SearchTerm startTerm = new ReceivedDateTerm(ComparisonTerm.GE, startDate);
            SearchTerm endTerm = new ReceivedDateTerm(ComparisonTerm.LT, endDate);
            terms.add(new AndTerm(startTerm, endTerm));
            log.debug("Pull Date: StartTime: {}, EndTime:{}", startTime, endTime);
        }

        if (terms.isEmpty()) return null;
        if (terms.size() == 1) return terms.get(0);

        return new AndTerm(terms.toArray(new SearchTerm[0]));
    }

    /**
     * Extract the email body (only handles simple cases like text/plain and text/html)
     */
    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("text/html")) {
            // 使用 Jsoup 解析 HTML 为纯文本
            return Jsoup.parse(message.getContent().toString()).text();
        } else if (message.isMimeType("multipart/*")) {
            return getTextFromMultipart((Multipart) message.getContent());
        }
        return "";
    }

    private static String getTextFromMultipart(Multipart multipart) throws Exception {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.isMimeType("text/plain")) {
                return part.getContent().toString();
            } else if (part.isMimeType("text/html")) {
                // HTML 转纯文本
                return Jsoup.parse(part.getContent().toString()).text();
            } else if (part.isMimeType("multipart/*")) {
                String result = getTextFromMultipart((Multipart) part.getContent());
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
        }
        return "";
    }

    /**
     * Extract emails with personal name and address, return JSON array string
     */
    private static String extractEmails(Address[] addresses) {
        if (addresses == null || addresses.length == 0) return null;
        JSONArray list = new JSONArray();
        for (Address addr : addresses) {
            if (addr instanceof InternetAddress) {
                InternetAddress iAddr = (InternetAddress) addr;
                list.add(new EmailContact(
                        iAddr.getPersonal(),
                        iAddr.getAddress()
                ));
            } else {
                list.add(new EmailContact(null, addr.toString()));
            }
        }
        return list.toJSONString();
    }
}
