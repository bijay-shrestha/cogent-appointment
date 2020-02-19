package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.EmailTemplates;
import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.constants.StringConstant;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.repository.EmailToSendRepository;
import com.cogent.cogentappointment.admin.service.EmailService;
import com.cogent.cogentappointment.persistence.model.EmailToSend;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.*;
import static com.cogent.cogentappointment.admin.constants.EmailConstants.ForgotPassword.RESET_CODE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.EmailLog.*;
import static com.cogent.cogentappointment.admin.utils.EmailUtils.convertDTOToEmailToSend;
import static com.cogent.cogentappointment.admin.utils.EmailUtils.convertToUpdateEmailToSend;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 7/23/19
 */
@Service
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final EmailToSendRepository emailToSendRepository;

    private final JavaMailSender javaMailSender;

    private final Configuration configuration;

    public EmailServiceImpl(EmailToSendRepository emailToSendRepository,
                            JavaMailSender javaMailSender,
                            Configuration configuration) {
        this.emailToSendRepository = emailToSendRepository;
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    @Override
    public void sendEmail(EmailRequestDTO emailRequestDTO) {
        EmailToSend emailToSend = saveEmailToSend(emailRequestDTO);
        send(emailToSend);
        updateEmailToSend(emailToSend);
    }

    private EmailToSend saveEmailToSend(EmailRequestDTO emailRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, EMAIL_TO_SEND);

        EmailToSend emailToSend = emailToSendRepository.save(convertDTOToEmailToSend(emailRequestDTO));

        log.info(SAVING_PROCESS_COMPLETED, EMAIL_TO_SEND, getDifferenceBetweenTwoTime(startTime));

        return emailToSend;
    }

    private void send(EmailToSend emailToSend) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SENDING_EMAIL_PROCESS_STARTED);

        try {
            MimeMessage message = getMimeMessage(emailToSend);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Map<String, Object> model = new HashMap<>();
            String html = "";

            switch (emailToSend.getTemplateName()) {
                case EmailTemplates.ADMIN_VERIFICATION: {
                    parseToAdminVerificationTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, EmailTemplates.ADMIN_VERIFICATION_TEMPLATE, html);
                    break;
                }

                case EmailTemplates.ADMIN_RESET_PASSWORD: {
                    parseToResetPasswordTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, EmailTemplates.ADMIN_RESET_PASSWORD_TEMPLATE, html);
                    break;
                }

                case EmailTemplates.UPDATE_ADMIN: {
                    parseToUpdateAdminTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, EmailTemplates.UPDATE_ADMIN_TEMPLATE, html);
                    break;
                }

                case EmailTemplates.FORGOT_PASSWORD: {
                    parseToForgotPasswordTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, EmailTemplates.FORGOT_PASSWORD_TEMPLATE, html);
                    break;
                }

                default:
                    break;
            }

            helper.setText(html, true);
            helper.addInline(LOGO_FILE_NAME, new FileSystemResource(new File(LOGO_LOCATION)));

            javaMailSender.send(message);

            log.info(SENDING_EMAIL_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private MimeMessage getMimeMessage(EmailToSend emailToSend) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailToSend.getReceiverEmailAddress()));
        message.setSubject(emailToSend.getSubject());
        return message;
    }

    private String getFreeMarkerContent(Map<String, Object> model, String templateName, String html) {
        try {
            Template template = configuration.getTemplate(templateName);
            html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return html;
    }

    private void updateEmailToSend(EmailToSend emailToSend) {
        emailToSendRepository.save(convertToUpdateEmailToSend(emailToSend));
    }

    private void parseToAdminVerificationTemplate(EmailToSend emailToSend,
                                                  Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int ADMIN_CONFIRMATION_URL_INDEX = 1;

        String[] paramValues = emailToSend.getParamValue().split(StringConstant.COMMA_SEPARATED);

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(Admin.ADMIN_CONFIRMATION_URL, paramValues[ADMIN_CONFIRMATION_URL_INDEX]);
    }

    private void parseToResetPasswordTemplate(EmailToSend emailToSend,
                                              Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int PASSWORD_INDEX = 1;
        final int REMARKS_INDEX = 2;

        String[] paramValues = emailToSend.getParamValue().split(StringConstant.COMMA_SEPARATED);

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(Admin.PASSWORD, paramValues[PASSWORD_INDEX]);
        model.put(Admin.REMARKS, paramValues[REMARKS_INDEX]);
    }

    private void parseToUpdateAdminTemplate(EmailToSend emailToSend,
                                            Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int UPDATED_ADMIN_DETAILS_INDEX = 1;
        final int HAS_MAC_BINDING_INDEX = 2;
        final int MAC_ADDRESS_INDEX = 3;

        String param = emailToSend.getParamValue().replaceAll(StringConstant.BRACKET_REGEX, "");
        String[] paramValues = param.split(StringConstant.HYPHEN);
        String[] updatedValues = paramValues[UPDATED_ADMIN_DETAILS_INDEX].split(StringConstant.COMMA_SEPARATED);

        String[] updatedMacAddress = paramValues[HAS_MAC_BINDING_INDEX].equals(StatusConstants.YES) ?
                paramValues[MAC_ADDRESS_INDEX].split(StringConstant.COMMA_SEPARATED)
                : new String[]{paramValues[MAC_ADDRESS_INDEX]};

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(Admin.UPDATED_DATA, Arrays.asList(updatedValues));
        model.put(Admin.HAS_MAC_BINDING, paramValues[HAS_MAC_BINDING_INDEX]);
        model.put(Admin.UPDATED_MAC_ADDRESS, Arrays.asList(updatedMacAddress));
    }

    private void parseToForgotPasswordTemplate(EmailToSend emailToSend, Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int RESET_CODE_INDEX = 1;

        String[] paramValues = emailToSend.getParamValue().split(StringConstant.COMMA_SEPARATED);
        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(RESET_CODE, paramValues[RESET_CODE_INDEX]);
    }
}
