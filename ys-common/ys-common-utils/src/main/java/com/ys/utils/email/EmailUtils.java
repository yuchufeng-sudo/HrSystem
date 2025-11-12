package com.ys.utils.email;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.ys.utils.constant.RedisConstants;
import com.ys.utils.core.redis.RedisCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

@Component
@Slf4j
@Data
public class EmailUtils {

//    @Value("${spring.mail.username}")
    private String mailUserName = "2729623478@qq.com";
    // 邮件协议
    private static final String emailProtocol = "smtp";
    // 发件人的SMTP服务器地址（企业邮箱）
    private static final String emailSMTPHost = "smtp.qq.com";
    // 端口
    private static final String emailPort = "465";
//    // 发件人邮箱地址
//    private static final String emailAccount = "2795051032@qq.com";
//    // 发件人邮箱授权码
//    private static final String emailPassword = "hkyindmkligudfcb";
    // 发件人邮箱地址
    private static final String emailAccount = "2729623478@qq.com";
    // 发件人邮箱授权码
    private static final String emailPassword = "shhcrwocaykxdghc";

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RedisCache redisCache;


    public Boolean sendEmail(String email,String type) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // 使用smtp身份验证
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            // 1. Generate verification code
            String code = getNewcode();

            // 2. Prepare email context
            Context context = new Context();
            context.setVariable("code", code);
            String htmlContent = "";
            if (type.equals("1")){
                redisCache.setCacheObject(
                        RedisConstants.EMAIL_CAPTCHA + email,
                        code,
                        RedisConstants.EMAIL_CAPTCHA_VALIDITY
                );
                htmlContent = templateEngine.process("changepassword-verification.html", context);
            }else {
                redisCache.setCacheObject(
                        RedisConstants.EMAIL_CAPTCHA_2FA + email,
                        code,
                        RedisConstants.EMAIL_CAPTCHA_VALIDITY
                );
                htmlContent = templateEngine.process("two-face.html", context);
            }

            // 3. Build MIME message
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setFrom(mailUserName);
            message.setTo(email);
            if("1".equals(type)){
                message.setSubject("CRM-System Password Reset");
            }else{
                message.setSubject("Authentication Request");
            }
            message.setSentDate(new Date());
            message.setText(htmlContent, true);

            // 4. Send email
            mailSender.send(mimeMessage);
            System.out.println(code);

            // 5. Log verification code (建议改为Logger.debug)
            log.debug("Sent verification code {} to {}", code, email);
            return true;

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }

    @Async
    public Boolean sendEmailByTemplate(Map<String, Object> map, String email , String type) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // 使用smtp身份验证
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            Context context = new Context();
            String htmlContent = "";
            String Subject = "";
            StringTemplateResolver stringResolver = new StringTemplateResolver();
            stringResolver.setTemplateMode("HTML");
            stringResolver.setCacheable(false);
            TemplateEngine stringEngine = new TemplateEngine();
            stringEngine.setTemplateResolver(stringResolver);
            switch (type) {
                case "Candidate-Application":
                    Subject = "New Candidate Application Received";
                    context.setVariable("HrName", map.get("HrName"));
                    context.setVariable("JobTitle", map.get("JobTitle"));
                    context.setVariable("FullName", map.get("FullName"));
                    context.setVariable("CandidateEmail", map.get("CandidateEmail"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("candidate-application.html", context);
                    break;
                case "Candidate-Shortlisted":
                    Subject = "Congratulations! You've Been Shortlisted";
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("JobTitle", map.get("JobTitle"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    htmlContent = templateEngine.process("candidate-shortlisted.html", context);
                    break;
                case "EmployeeAccountCreatedManually":
                    String companyName = Objects.toString(map.get("CompanyName"), "");
                    Subject = "Your " + companyName + " CRM Account Is Ready";
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("Username", map.get("Username"));
                    context.setVariable("Password", map.get("Password"));
                    htmlContent = templateEngine.process("employee-account.html", context);
//                    htmlContent = stringEngine.process(dbTemplate, context);
                    break;
                case "CandidateHired":
                    String companyName2 = Objects.toString(map.get("CompanyName"), "");
                    Subject = "Congratulations – You’ve Been Hired at " + companyName2 + "!";
                    context.setVariable("SupportEmail", map.get("SupportEmail"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("HrName", map.get("HrName"));
                    context.setVariable("HrEmail", map.get("HrEmail"));
                    context.setVariable("FirstName", map.get("FirstName"));
                    htmlContent = templateEngine.process("candidate-hired.html", context);
                    break;
                case "Interview":
                    String JobTitle = Objects.toString(map.get("JobTitle"), "");
                    Subject = "Invitation to Interview for " + JobTitle;
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("JobTitle", map.get("JobTitle"));
                    context.setVariable("InterviewDate", map.get("InterviewDate"));
                    context.setVariable("InterviewTime", map.get("InterviewTime"));
                    context.setVariable("InterviewLocation", map.get("InterviewLocation"));
                    htmlContent = templateEngine.process("candidate-invite.html", context);
                    break;
                case "Invite":
                    String companyName3 = Objects.toString(map.get("CompanyName"), "");
                    Subject = "You're Invited to Join " + companyName3 + "'s HR Platform!";
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("InviteUrl", map.get("InviteUrl"));
                    context.setVariable("HrisToolName", map.get("HrisToolName"));
                    htmlContent = templateEngine.process("Invite.html", context);
                    break;
                case "Application":
                    String companyName4 = Objects.toString(map.get("CompanyName"), "");
                    Subject = "New Company Onboarding Request Received - " + companyName4 + " !";
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("EmailAddress",map.get("EmailAddress"));
                    context.setVariable("PhoneNumber",map.get("PhoneNumber"));
                    context.setVariable("SubmissionDate",map.get("SubmissionDate"));
                    context.setVariable("PendingApprovalsUrl",map.get("PendingApprovalsUrl"));
                    htmlContent = templateEngine.process("application.html", context);
                    break;
                case "NewCompany":
                    String companyName5 = Objects.toString(map.get("CompanyName"), "");
                    Subject = "Welcome to " + companyName5 + " – Let’s Get You Started!";
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    context.setVariable("LoginUrl",map.get("LoginUrl"));
                    context.setVariable("UserName",map.get("UserName"));
                    context.setVariable("PassWord",map.get("PassWord"));
                    htmlContent = templateEngine.process("new-company.html", context);
                    break;
                case "OffboardingInitiated":
                    Subject = "Offboarding Checklist Initiated";
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("OffboardingUrl",map.get("OffboardingUrl"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    htmlContent = templateEngine.process("offboarding-initiated.html", context);
                    break;
                case "TerminationComplete":
                    Subject = "Termination Notice – Account Deactivated";
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("HrEmail",map.get("HrEmail"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    htmlContent = templateEngine.process("termination-complete.html", context);
                    break;
                case "offboardingReminderWeekHr":
                    Subject = "Reminder: Offboarding for "+map.get("FullName")+" Due in 1 Week";
                    context.setVariable("HrFullName",map.get("HrFullName"));
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("OffboardingUrl",map.get("OffboardingUrl"));
                    htmlContent = templateEngine.process("offboarding-reminder-week-hr.html", context);
                    break;
                case "offboardingReminderWeekEmployee":
                    Subject = "Reminder: Complete Your Offboarding Tasks";
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    context.setVariable("OffboardingUrl",map.get("OffboardingUrl"));
                    htmlContent = templateEngine.process("offboarding-reminder-week-employee.html", context);
                    break;
                case "offboardingReminderDayHr":
                    Subject = "Final Reminder: Offboarding for "+map.get("FullName")+" Due Tomorrow";
                    context.setVariable("HrFullName",map.get("HrFullName"));
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("OffboardingUrl",map.get("OffboardingUrl"));
                    htmlContent = templateEngine.process("offboarding-reminder-day-hr.html", context);
                    break;
                case "offboardingReminderDayEmployee":
                    Subject = "Final Reminder: Offboarding Tasks Due Tomorrow";
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("HrisToolName",map.get("HrisToolName"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    context.setVariable("OffboardingUrl",map.get("OffboardingUrl"));
                    htmlContent = templateEngine.process("offboarding-reminder-day-employee.html", context);
                    break;
                case "offboardingComplete":
                    Subject = "Offboarding Checklist Completed";
                    context.setVariable("FullName",map.get("FullName"));
                    context.setVariable("CompanyName",map.get("CompanyName"));
                    htmlContent = templateEngine.process("offboarding-complete.html", context);
                    break;
                default:
                    log.error("Failed to send email to {}", email);
                    return false;
            }

            // 3. Build MIME message
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUserName);
            message.setTo(email);
            message.setSubject(Subject);
            message.setSentDate(new Date());
            message.setText(htmlContent, true);

            // 4. Send email
            mailSender.send(mimeMessage);

            return true;

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }

    @Async
    public Boolean sendEmailByEmailTemplate(Map<String, Object> map, String email, String dbTemplate,Map<String, Object> subjectParams, String subjectTemplate) {
        try {
            // 1. 邮件配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);

            // 2. 用 Mustache 渲染数据库模板
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(dbTemplate), "emailTemplate");

            StringWriter writer = new StringWriter();
            mustache.execute(writer, map).flush();
            String htmlContent = writer.toString();

            // 3. 主题可以从 map 或者另一个字段里取，这里先写死
//            String subject = map.getOrDefault("subject", "系统通知").toString();
            String subject = fillTemplate(subjectTemplate,subjectParams);

            // 4. 构建 MIME 邮件
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUserName);
            message.setTo(email);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(htmlContent, true);
            // 5. 发送
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }

    @Async
    public Boolean sendEmailByEmailTemplate(
            Map<String, Object> map,
            String email,
            String dbTemplate,
            Map<String, Object> subjectParams,
            String subjectTemplate,
            String type,               // 新增参数：TO / CC / BCC
            String[] ccEmails,         // 抄送列表，可为空
            String[] bccEmails         // 密送列表，可为空
    ) {
        try {
            // 1. 邮件配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);

            // 2. 用 Mustache 渲染数据库模板
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile(new StringReader(dbTemplate), "emailTemplate");

            StringWriter writer = new StringWriter();
            mustache.execute(writer, map).flush();
            String htmlContent = writer.toString();

            // 3. 主题渲染
            String subject = fillTemplate(subjectTemplate, subjectParams);

            // 4. 构建 MIME 邮件
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUserName);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(htmlContent, true);

            // 5. 根据 type 设置收件人、抄送、密送
            switch ((type != null ? type.toUpperCase() : "TO")) {
                case "CC":
                    if (ccEmails != null && ccEmails.length > 0) {
                        message.setTo(ccEmails);   // CC 也可以作为主要收件人
                    } else {
                        message.setTo(email);
                    }
                    break;
                case "BCC":
                    if (bccEmails != null && bccEmails.length > 0) {
                        message.setTo(bccEmails);  // BCC 也作为收件人
                    } else {
                        message.setTo(email);
                    }
                    break;
                default:
                    message.setTo(email);
                    break;
            }

            // 6. 设置附加抄送/密送列表
            if (ccEmails != null && ccEmails.length > 0) {
                message.setCc(ccEmails);
            }
            if (bccEmails != null && bccEmails.length > 0) {
                message.setBcc(bccEmails);
            }

            // 7. 发送
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }

    public Boolean sendStaticEmail(String email, int type, Map<String, Object> params) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // 使用smtp身份验证
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            // 2. Prepare email context
            Context context = new Context();
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailUserName);
            message.setTo(email);
            if(type == 1){
                message.setSubject("Thanks for signing up – we’re reviewing your request");
                context.setVariable("fullName",params.get("fullName"));
                String text = templateEngine.process("customer-sign-up.html", context);
                message.setText(text, true);
            }
            if(type == 2){
                message.setSubject("New signup request awaiting review");
                context.setVariable("adminName",params.get("adminName"));
                context.setVariable("companyName",params.get("companyName"));
                context.setVariable("contactName",params.get("contactName"));
                context.setVariable("email",params.get("email"));
                String text = templateEngine.process("new-onboarding-request.html", context);
                message.setText(text, true);
            }
            if (type == 3){
                message.setSubject("Welcome aboard – your account is ready!");
                context.setVariable("fullName",params.get("fullName"));
                String text = templateEngine.process("customer-approved.html", context);
                message.setText(text, true);
            }
            if (type == 4){
                message.setSubject("Update on your signup request");
                context.setVariable("fullName",params.get("fullName"));
                String text = templateEngine.process("customer-rejected.html", context);
                message.setText(text, true);
            }
            if (type == 5){
                message.setSubject("Your account has been disabled");
                context.setVariable("fullName",params.get("fullName"));
                String text = templateEngine.process("customer-disable.html", context);
                message.setText(text, true);
            }
            if (type == 6){
                message.setSubject("Your password has been reset");
                context.setVariable("fullName",params.get("fullName"));
                context.setVariable("code",params.get("code"));
                context.setVariable("type",params.get("type"));
                String text = templateEngine.process("customer-reset-password.html", context);
                message.setText(text, true);
            }
            if (type == 7){
                message.setSubject("You’re invited to join TeohCRM");
                context.setVariable("customerName",params.get("customerName"));
                context.setVariable("code",params.get("code"));
                context.setVariable("type",params.get("type"));
                context.setVariable("message",params.get("message"));
                String text = templateEngine.process("customer_invites_user.html", context);
                message.setText(text, true);
            }
            if (type == 8){
                message.setSubject("Update on your signup request");
                context.setVariable("fullName",params.get("fullName"));
                context.setVariable("companyName",params.get("companyName"));
                context.setVariable("companyEmail",params.get("companyEmail"));
                String text = templateEngine.process("user-rejected.html", context);
                message.setText(text, true);
            }
            message.setSentDate(new Date());
            // 4. Send email
            mailSender.send(mimeMessage);

            log.debug("Sent Params {} to {}", params, email);
            return true;

        } catch (MessagingException e) {
            log.error("Failed to send email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }

    @Async
    public Boolean sendEmailByEmailTemplate(
            Map<String, Object> map,
            String[] toEmails,
            String dbTemplate,
            Map<String, Object> subjectParams,
            String subjectTemplate,
            String[] ccEmails,
            String[] bccEmails,
            List<File> attachments
    ) {
        try {
            // 1. 邮件配置
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            props.setProperty("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.connectiontimeout", "10000");
            props.setProperty("mail.smtp.timeout", "10000");
            props.setProperty("mail.smtp.writetimeout", "10000");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);

            // 2. 渲染数据库模板 (HTML 内容)
//            MustacheFactory mf = new DefaultMustacheFactory();
//            Mustache mustache = mf.compile(new StringReader(dbTemplate), "emailTemplate");

//            StringWriter writer = new StringWriter();
//            mustache.execute(writer, map).flush();
//            String htmlContent = writer.toString();

            // 3. 渲染邮件主题
//            String subject = fillTemplate(subjectTemplate, subjectParams);

            // 4. 构建 MIME 邮件
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true 表示 multipart
            message.setFrom(mailUserName);
            message.setSubject(subjectTemplate);
            message.setSentDate(new Date());
            message.setText(dbTemplate, true);

            // 5. 设置收件人、抄送、密送
            if (toEmails != null && toEmails.length > 0) {
                message.setTo(toEmails);
            }
            if (ccEmails != null && ccEmails.length > 0) {
                message.setCc(ccEmails);
            }
            if (bccEmails != null && bccEmails.length > 0) {
                message.setBcc(bccEmails);
            }

            // 6. 添加附件
            if (attachments != null && !attachments.isEmpty()) {
                for (File file : attachments) {
                    if (file != null && file.exists()) {
                        message.addAttachment(file.getName(), file);
                    }
                }
            }

            // 7. 发送
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", Arrays.toString(toEmails), e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending email", e);
            return false;
        }
    }


    public static String fillTemplate(String template, Map<String, Object> params) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(template), "template");

        StringWriter writer = new StringWriter();
        mustache.execute(writer, params).flush();

        return writer.toString();
    }


    public static String getNewcode() {
        return String.valueOf((new Random().nextInt(8999) + 1000));
    }
}
