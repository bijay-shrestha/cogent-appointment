package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.constants.EmailConstants;
import com.cogent.cogentappointment.client.constants.EmailTemplates;
import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.constants.StringConstant;
import com.cogent.cogentappointment.client.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.client.log.CommonLogConstant;
import com.cogent.cogentappointment.client.model.EmailToSend;
import com.cogent.cogentappointment.client.repository.EmailToSendRepository;
import com.cogent.cogentappointment.client.service.EmailService;
import com.cogent.cogentappointment.client.utils.commons.DateUtils;
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

import static com.cogent.cogentappointment.client.log.constants.EmailLog.*;
import static com.cogent.cogentappointment.client.utils.EmailUtils.convertDTOToEmailToSend;
import static com.cogent.cogentappointment.client.utils.EmailUtils.convertToUpdateEmailToSend;

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

    public EmailToSend saveEmailToSend(EmailRequestDTO emailRequestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.SAVING_PROCESS_STARTED, EMAIL_TO_SEND);

        EmailToSend emailToSend = emailToSendRepository.save(convertDTOToEmailToSend(emailRequestDTO));

        log.info(CommonLogConstant.SAVING_PROCESS_COMPLETED, EMAIL_TO_SEND, DateUtils.getDifferenceBetweenTwoTime(startTime));

        return emailToSend;
    }

    public void send(EmailToSend emailToSend) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

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
            helper.addInline(EmailConstants.LOGO_FILE_NAME, new FileSystemResource(new File(EmailConstants.LOGO_LOCATION)));

            javaMailSender.send(message);

            log.info(SENDING_EMAIL_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));
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

        model.put(EmailConstants.USERNAME, paramValues[USERNAME_INDEX]);
        model.put(EmailConstants.Admin.ADMIN_CONFIRMATION_URL, paramValues[ADMIN_CONFIRMATION_URL_INDEX]);
    }

    private void parseToResetPasswordTemplate(EmailToSend emailToSend,
                                              Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int PASSWORD_INDEX = 1;
        final int REMARKS_INDEX = 2;

        String[] paramValues = emailToSend.getParamValue().split(StringConstant.COMMA_SEPARATED);

        model.put(EmailConstants.USERNAME, paramValues[USERNAME_INDEX]);
        model.put(EmailConstants.Admin.PASSWORD, paramValues[PASSWORD_INDEX]);
        model.put(EmailConstants.Admin.REMARKS, paramValues[REMARKS_INDEX]);
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

        model.put(EmailConstants.USERNAME, paramValues[USERNAME_INDEX]);
        model.put(EmailConstants.Admin.UPDATED_DATA, Arrays.asList(updatedValues));
        model.put(EmailConstants.Admin.HAS_MAC_BINDING, paramValues[HAS_MAC_BINDING_INDEX]);
        model.put(EmailConstants.Admin.UPDATED_MAC_ADDRESS, Arrays.asList(updatedMacAddress));
    }

    private void parseToForgotPasswordTemplate(EmailToSend emailToSend, Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int RESET_CODE_INDEX = 1;

        String[] paramValues = emailToSend.getParamValue().split(StringConstant.COMMA_SEPARATED);
        model.put(EmailConstants.USERNAME, paramValues[USERNAME_INDEX]);
        model.put(EmailConstants.ForgotPassword.RESET_CODE, paramValues[RESET_CODE_INDEX]);
    }
}
