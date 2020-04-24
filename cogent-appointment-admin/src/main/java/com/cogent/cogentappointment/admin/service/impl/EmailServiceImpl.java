package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.property.EmailProperties;
import com.cogent.cogentappointment.admin.repository.EmailToSendRepository;
import com.cogent.cogentappointment.admin.service.EmailService;
import com.cogent.cogentappointment.admin.utils.commons.FileResourceUtils;
import com.cogent.cogentappointment.persistence.model.EmailToSend;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.io.IOException;
import java.util.*;

import static com.cogent.cogentappointment.admin.constants.EmailConstants.Admin.*;
import static com.cogent.cogentappointment.admin.constants.EmailConstants.ForgotPassword.RESET_CODE;
import static com.cogent.cogentappointment.admin.constants.EmailConstants.*;
import static com.cogent.cogentappointment.admin.constants.EmailTemplates.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.constants.StringConstant.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.EmailLog.*;
import static com.cogent.cogentappointment.admin.utils.EmailUtils.convertDTOToEmailToSend;
import static com.cogent.cogentappointment.admin.utils.EmailUtils.updateEmailToSendStatus;
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

    private final EmailProperties emailProperties;

    public EmailServiceImpl(EmailToSendRepository emailToSendRepository,
                            @Qualifier("getMailSender") JavaMailSender javaMailSender,
                            Configuration configuration,
                            EmailProperties emailProperties) {
        this.emailToSendRepository = emailToSendRepository;
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
        this.emailProperties = emailProperties;
    }

    @Override
    public EmailToSend saveEmailToSend(EmailRequestDTO emailRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, EMAIL_TO_SEND);

        EmailToSend emailToSend = emailToSendRepository.save(convertDTOToEmailToSend(emailRequestDTO));

        log.info(SAVING_PROCESS_COMPLETED, EMAIL_TO_SEND, getDifferenceBetweenTwoTime(startTime));

        return emailToSend;
    }

    @Override
    public void sendEmail(EmailRequestDTO emailRequestDTO) {
        EmailToSend emailToSend = saveEmailToSend(emailRequestDTO);
        send(emailToSend);
        updateEmailToSendStatus(emailToSend);
    }

    @Override
    public void sendEmail() {

        if(emailProperties.getEnabled().equals(YES)){
            Long startTime = getTimeInMillisecondsFromLocalDate();

            log.info(SENDING_EMAIL_PROCESS_STARTED);

            List<EmailToSend> unsentEmails = emailToSendRepository.fetchUnsentEmails();

            unsentEmails.forEach(unsentEmail -> {
                send(unsentEmail);
                updateEmailToSendStatus(unsentEmail);
            });

            log.info(SENDING_EMAIL_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
        }
    }

    private void send(EmailToSend emailToSend) {

        try {
            MimeMessage message = getMimeMessage(emailToSend);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Map<String, Object> model = new HashMap<>();
            String html = "";

            switch (emailToSend.getTemplateName()) {
                case ADMIN_VERIFICATION: {
                    parseToAdminVerificationTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, ADMIN_VERIFICATION_TEMPLATE, html);
                    break;
                }

                case EMAIL_VERIFICATION: {
                    parseToAdminVerificationTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, EMAIL_VERIFICATION_TEMPLATE, html);
                    break;
                }

                case ADMIN_RESET_PASSWORD: {
                    parseToResetPasswordTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, ADMIN_RESET_PASSWORD_TEMPLATE, html);
                    break;
                }

                case UPDATE_ADMIN: {
                    parseToUpdateAdminTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, UPDATE_ADMIN_TEMPLATE, html);
                    break;
                }

                case FORGOT_PASSWORD: {
                    parseToForgotPasswordTemplate(emailToSend, model);
                    html = getFreeMarkerContent(model, FORGOT_PASSWORD_TEMPLATE, html);
                    break;
                }

                default:
                    break;
            }

            helper.setText(html, true);

            helper.addInline(LOGO_FILE_NAME, new FileSystemResource
                    (new FileResourceUtils().convertResourcesFileIntoFile(LOGO_LOCATION)));

            javaMailSender.send(message);
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

    private void parseToAdminVerificationTemplate(EmailToSend emailToSend,
                                                  Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int ADMIN_CONFIRMATION_URL_INDEX = 1;

        String[] paramValues = emailToSend.getParamValue().split(COMMA_SEPARATED);

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(ADMIN_CONFIRMATION_URL, paramValues[ADMIN_CONFIRMATION_URL_INDEX]);
    }

    private void parseToResetPasswordTemplate(EmailToSend emailToSend,
                                              Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int PASSWORD_INDEX = 1;
        final int REMARKS_INDEX = 2;

        String[] paramValues = emailToSend.getParamValue().split(COMMA_SEPARATED);

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(PASSWORD, paramValues[PASSWORD_INDEX]);
        model.put(REMARKS, paramValues[REMARKS_INDEX]);
    }

    private void parseToUpdateAdminTemplate(EmailToSend emailToSend,
                                            Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int UPDATED_ADMIN_DETAILS_INDEX = 1;
        final int HAS_MAC_BINDING_INDEX = 2;
        final int MAC_ADDRESS_INDEX = 3;

        String param = emailToSend.getParamValue().replaceAll(BRACKET_REGEX, SPACE);
        String[] paramValues = param.split(HYPHEN);
        String[] updatedValues = paramValues[UPDATED_ADMIN_DETAILS_INDEX].split(COMMA_SEPARATED);

        String[] updatedMacAddress = Objects.equals(paramValues[HAS_MAC_BINDING_INDEX], "Y") ?
                paramValues[MAC_ADDRESS_INDEX].split(COMMA_SEPARATED)
                : new String[]{paramValues[MAC_ADDRESS_INDEX]};

        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(UPDATED_DATA, Arrays.asList(updatedValues));
        model.put(HAS_MAC_BINDING, paramValues[HAS_MAC_BINDING_INDEX]);
        model.put(UPDATED_MAC_ADDRESS, Arrays.asList(updatedMacAddress));
    }

    private void parseToForgotPasswordTemplate(EmailToSend emailToSend, Map<String, Object> model) {
        final int USERNAME_INDEX = 0;
        final int RESET_CODE_INDEX = 1;

        String[] paramValues = emailToSend.getParamValue().split(COMMA_SEPARATED);
        model.put(USERNAME, paramValues[USERNAME_INDEX]);
        model.put(RESET_CODE, paramValues[RESET_CODE_INDEX]);
    }
}
