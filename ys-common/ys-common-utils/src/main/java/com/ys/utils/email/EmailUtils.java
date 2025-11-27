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

/**
 * Email utility class for sending various types of emails including verification codes,
 * template-based notifications, and custom email templates with attachments.
 */
@Component
@Slf4j
@Data
public class EmailUtils {

    private String mailUserName = "2729623478@qq.com";
    // Email protocol
    private static final String emailProtocol = "smtp";
    // SMTP server address for sender (enterprise email)
    private static final String emailSMTPHost = "smtp.qq.com";
    // SMTP port
    private static final String emailPort = "465";

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private RedisCache redisCache;


    /**
     * Send verification code email for password reset or two-factor authentication
     * @param email Recipient email address
     * @param type Email type: "1" for password reset, other values for 2FA
     * @return true if email sent successfully, false otherwise
     */
    public Boolean sendEmail(String email,String type) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // Enable SMTP authentication
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

            // 5. Log verification code (recommended to use Logger.debug)
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

    /**
     * Send email using predefined templates for various scenarios
     * @param map Data map containing template variables
     * @param email Recipient email address
     * @param type Template type identifier (e.g., "Candidate-Application", "EmployeeAccountCreatedManually")
     * @return true if email sent successfully, false otherwise
     */
    @Async
    public Boolean sendEmailByTemplate(Map<String, Object> map, String email , String type) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // Enable SMTP authentication
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
                    context.setVariable("LoginUrl", map.get("LoginUrl"));
                    htmlContent = templateEngine.process("employee-account.html", context);
                    break;
                case "CandidateHired":
                    String companyName2 = Objects.toString(map.get("CompanyName"), "");
                    Subject = "Congratulations! You've Been Hired at " + companyName2;
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("JobTitle", map.get("JobTitle"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("StartDate", map.get("StartDate"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("candidate-hired.html", context);
                    break;
                case "InterviewScheduled":
                    Subject = "Interview Scheduled for " + map.get("JobTitle");
                    context.setVariable("FirstName", map.get("FirstName"));
                    context.setVariable("JobTitle", map.get("JobTitle"));
                    context.setVariable("InterviewDate", map.get("InterviewDate"));
                    context.setVariable("InterviewTime", map.get("InterviewTime"));
                    context.setVariable("InterviewLocation", map.get("InterviewLocation"));
                    context.setVariable("InterviewerName", map.get("InterviewerName"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    htmlContent = templateEngine.process("interview-scheduled.html", context);
                    break;
                case "LeaveRequestApproved":
                    Subject = "Your Leave Request Has Been Approved";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("LeaveType", map.get("LeaveType"));
                    context.setVariable("StartDate", map.get("StartDate"));
                    context.setVariable("EndDate", map.get("EndDate"));
                    context.setVariable("ApproverName", map.get("ApproverName"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("leave-request-approved.html", context);
                    break;
                case "LeaveRequestRejected":
                    Subject = "Your Leave Request Has Been Rejected";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("LeaveType", map.get("LeaveType"));
                    context.setVariable("StartDate", map.get("StartDate"));
                    context.setVariable("EndDate", map.get("EndDate"));
                    context.setVariable("RejectionReason", map.get("RejectionReason"));
                    context.setVariable("ApproverName", map.get("ApproverName"));
                    htmlContent = templateEngine.process("leave-request-rejected.html", context);
                    break;
                case "OnboardingWelcome":
                    Subject = "Welcome to " + map.get("CompanyName") + "! Let's Get Started";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("StartDate", map.get("StartDate"));
                    context.setVariable("HRContactName", map.get("HRContactName"));
                    context.setVariable("HRContactEmail", map.get("HRContactEmail"));
                    htmlContent = templateEngine.process("onboarding-welcome.html", context);
                    break;
                case "PasswordResetRequest":
                    Subject = "Password Reset Request";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("ResetLink", map.get("ResetLink"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("password-reset-request.html", context);
                    break;
                case "PayrollProcessed":
                    Subject = "Your Payroll Has Been Processed";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("PayPeriod", map.get("PayPeriod"));
                    context.setVariable("NetPay", map.get("NetPay"));
                    context.setVariable("PayDate", map.get("PayDate"));
                    context.setVariable("ViewPayslipLink", map.get("ViewPayslipLink"));
                    htmlContent = templateEngine.process("payroll-processed.html", context);
                    break;
                case "PerformanceReviewReminder":
                    Subject = "Reminder: Upcoming Performance Review";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("ReviewDate", map.get("ReviewDate"));
                    context.setVariable("ReviewerName", map.get("ReviewerName"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("performance-review-reminder.html", context);
                    break;
                case "ShiftScheduleUpdate":
                    Subject = "Your Shift Schedule Has Been Updated";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("ShiftDate", map.get("ShiftDate"));
                    context.setVariable("ShiftTime", map.get("ShiftTime"));
                    context.setVariable("Location", map.get("Location"));
                    context.setVariable("ManagerName", map.get("ManagerName"));
                    htmlContent = templateEngine.process("shift-schedule-update.html", context);
                    break;
                case "TrainingAssigned":
                    Subject = "New Training Assigned: " + map.get("TrainingName");
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("TrainingName", map.get("TrainingName"));
                    context.setVariable("TrainingDate", map.get("TrainingDate"));
                    context.setVariable("TrainingTime", map.get("TrainingTime"));
                    context.setVariable("TrainingLocation", map.get("TrainingLocation"));
                    htmlContent = templateEngine.process("training-assigned.html", context);
                    break;
                case "WorkAnniversary":
                    Subject = "Happy Work Anniversary!";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("YearsOfService", map.get("YearsOfService"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    context.setVariable("ManagerName", map.get("ManagerName"));
                    htmlContent = templateEngine.process("work-anniversary.html", context);
                    break;
                case "GoalSetReminder":
                    Subject = "It's Time to Set Your Goals for the Quarter";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("Quarter", map.get("Quarter"));
                    context.setVariable("DeadlineDate", map.get("DeadlineDate"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("goal-set-reminder.html", context);
                    break;
                case "TimesheetApprovalPending":
                    Subject = "Timesheet Approval Pending";
                    context.setVariable("ManagerName", map.get("ManagerName"));
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("TimePeriod", map.get("TimePeriod"));
                    context.setVariable("TotalHours", map.get("TotalHours"));
                    context.setVariable("HrisToolName", "Shiftcare HR");
                    htmlContent = templateEngine.process("timesheet-approval-pending.html", context);
                    break;
                case "BenefitsEnrollmentReminder":
                    Subject = "Reminder: Enroll in Your Benefits";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("EnrollmentDeadline", map.get("EnrollmentDeadline"));
                    context.setVariable("EnrollmentLink", map.get("EnrollmentLink"));
                    context.setVariable("HRContactName", map.get("HRContactName"));
                    htmlContent = templateEngine.process("benefits-enrollment-reminder.html", context);
                    break;
                case "ComplianceTrainingDue":
                    Subject = "Compliance Training Due Soon";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("TrainingName", map.get("TrainingName"));
                    context.setVariable("DueDate", map.get("DueDate"));
                    context.setVariable("TrainingLink", map.get("TrainingLink"));
                    htmlContent = templateEngine.process("compliance-training-due.html", context);
                    break;
                case "EmployeeReferralBonus":
                    Subject = "You've Earned a Referral Bonus!";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("ReferredCandidateName", map.get("ReferredCandidateName"));
                    context.setVariable("BonusAmount", map.get("BonusAmount"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    htmlContent = templateEngine.process("employee-referral-bonus.html", context);
                    break;
                case "OffboardingChecklist":
                    Subject = "Important: Your Offboarding Checklist";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("LastWorkingDay", map.get("LastWorkingDay"));
                    context.setVariable("HRContactName", map.get("HRContactName"));
                    context.setVariable("HRContactEmail", map.get("HRContactEmail"));
                    htmlContent = templateEngine.process("offboarding-checklist.html", context);
                    break;
                case "SurveyRequest":
                    Subject = "We Value Your Feedback - Quick Survey";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("SurveyName", map.get("SurveyName"));
                    context.setVariable("SurveyLink", map.get("SurveyLink"));
                    context.setVariable("Deadline", map.get("Deadline"));
                    htmlContent = templateEngine.process("survey-request.html", context);
                    break;
                case "CompanyAnnouncementImportant":
                    Subject = "Important Company Announcement";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("AnnouncementTitle", map.get("AnnouncementTitle"));
                    context.setVariable("AnnouncementBody", map.get("AnnouncementBody"));
                    context.setVariable("CompanyName", map.get("CompanyName"));
                    htmlContent = templateEngine.process("company-announcement-important.html", context);
                    break;
                case "ProbationReviewScheduled":
                    Subject = "Your Probation Review is Scheduled";
                    context.setVariable("EmployeeName", map.get("EmployeeName"));
                    context.setVariable("ReviewDate", map.get("ReviewDate"));
                    context.setVariable("ReviewTime", map.get("ReviewTime"));
                    context.setVariable("ManagerName", map.get("ManagerName"));
                    htmlContent = templateEngine.process("probation-review-scheduled.html", context);
                    break;
                default:
                    log.warn("Unknown email template type: {}", type);
                    return false;
            }

            // Build MIME message
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setFrom(mailUserName);
            message.setTo(email);
            message.setSubject(Subject);
            message.setSentDate(new Date());
            message.setText(htmlContent, true);

            // Send email
            mailSender.send(mimeMessage);

            log.debug("Sent template email '{}' to {}", type, email);
            return true;

        } catch (MessagingException e) {
            log.error("Failed to send template email to {}", email, e);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error when sending template email", e);
            return false;
        }
    }

    /**
     * Send email for customer onboarding workflow
     * @param params Map containing email parameters
     * @param email Recipient email address
     * @param type Email type: 1=signup confirmation, 2=admin notification, 3=approval, 4=rejection, 5=disabled, 6=password reset, 7=invitation, 8=user rejection
     * @return true if email sent successfully, false otherwise
     */
    @Async
    public Boolean sendCustomerEmail(Map<String, Object> params, String email , Integer type) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", emailProtocol);
            props.setProperty("mail.smtp.host", emailSMTPHost);
            props.setProperty("mail.smtp.port", emailPort);
            // Enable SMTP authentication
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
                message.setSubject("Thanks for signing up – we're reviewing your request");
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
                message.setSubject("You're invited to join TeohCRM");
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

    /**
     * Send email using custom database template with support for multiple recipients, CC, BCC, and attachments
     * @param map Data map for template variables
     * @param toEmails Array of recipient email addresses
     * @param dbTemplate HTML content template
     * @param subjectParams Parameters for subject template (currently unused)
     * @param subjectTemplate Email subject
     * @param ccEmails Array of CC email addresses
     * @param bccEmails Array of BCC email addresses
     * @param attachments List of file attachments
     * @return true if email sent successfully, false otherwise
     */
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
            // 1. Email configuration
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

            // 2. Render database template (HTML content) - commented out Mustache rendering
            // MustacheFactory mf = new DefaultMustacheFactory();
            // Mustache mustache = mf.compile(new StringReader(dbTemplate), "emailTemplate");
            // StringWriter writer = new StringWriter();
            // mustache.execute(writer, map).flush();
            // String htmlContent = writer.toString();

            // 3. Render email subject - commented out template rendering
            // String subject = fillTemplate(subjectTemplate, subjectParams);

            // 4. Build MIME message
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true indicates multipart
            message.setFrom(mailUserName);
            message.setSubject(subjectTemplate);
            message.setSentDate(new Date());
            message.setText(dbTemplate, true);

            // 5. Set recipients, CC, BCC
            if (toEmails != null && toEmails.length > 0) {
                message.setTo(toEmails);
            }
            if (ccEmails != null && ccEmails.length > 0) {
                message.setCc(ccEmails);
            }
            if (bccEmails != null && bccEmails.length > 0) {
                message.setBcc(bccEmails);
            }

            // 6. Add attachments
            if (attachments != null && !attachments.isEmpty()) {
                for (File file : attachments) {
                    if (file != null && file.exists()) {
                        message.addAttachment(file.getName(), file);
                    }
                }
            }

            // 7. Send email
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


    /**
     * Fill template string with parameters using Mustache template engine
     * @param template Template string with Mustache placeholders
     * @param params Map of parameters to fill into template
     * @return Rendered template string
     * @throws IOException if template processing fails
     */
    public static String fillTemplate(String template, Map<String, Object> params) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(template), "template");

        StringWriter writer = new StringWriter();
        mustache.execute(writer, params).flush();

        return writer.toString();
    }


    /**
     * Generate a random 4-digit verification code
     * @return String containing 4-digit code (1000-9999)
     */
    public static String getNewcode() {
        return String.valueOf((new Random().nextInt(8999) + 1000));
    }
}
