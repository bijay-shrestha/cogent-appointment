package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.forgotPassword.ForgotPasswordRequestDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.property.ForgotPasswordProperties;
import com.cogent.cogentappointment.admin.repository.AdminRepository;
import com.cogent.cogentappointment.admin.repository.ForgotPasswordRepository;
import com.cogent.cogentappointment.admin.service.EmailService;
import com.cogent.cogentappointment.admin.service.ForgotPasswordService;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages.*;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ForgotPasswordMessages.RESET_CODE_EXPIRED;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.INVALID_VERIFICATION_TOKEN;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.INVALID_VERIFICATION_TOKEN_ERROR;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.admin.utils.ForgotPasswordUtils.convertToForgotPasswordVerification;
import static com.cogent.cogentappointment.admin.utils.ForgotPasswordUtils.parseToEmailRequestDTO;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static java.util.Objects.isNull;

/**
 * @author smriti on 2019-09-20
 */
@Service
@Transactional
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final AdminRepository adminRepository;

    private final ForgotPasswordProperties forgotPasswordProperties;

    private final ForgotPasswordRepository verificationRepository;

    private final EmailService emailService;

    public ForgotPasswordServiceImpl(AdminRepository adminRepository,
                                     ForgotPasswordProperties forgotPasswordProperties,
                                     ForgotPasswordRepository verificationRepository,
                                     EmailService emailService) {
        this.adminRepository = adminRepository;
        this.forgotPasswordProperties = forgotPasswordProperties;
        this.verificationRepository = verificationRepository;
        this.emailService = emailService;
    }

    @Override
    public void forgotPassword(String email) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FORGOT_PASSWORD_PROCESS_STARTED);

        Admin admin = adminRepository.fetchActiveAdminByEmail(email);

        validateAdmin(admin, email);

        ForgotPasswordVerification forgotPasswordVerification = verificationRepository.findByAdminId(admin.getId());

        forgotPasswordVerification = convertToForgotPasswordVerification(
                admin,
                forgotPasswordProperties.getExpiryTime(),
                isNull(forgotPasswordVerification) ? new ForgotPasswordVerification() : forgotPasswordVerification);

        save(forgotPasswordVerification);

        EmailRequestDTO emailRequestDTO = parseToEmailRequestDTO(
                admin.getEmail(),
                admin.getFullName(),
                forgotPasswordVerification.getResetCode());

        emailService.sendEmail(emailRequestDTO);

        log.info(FORGOT_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void verify(String resetCode) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(VERIFY_CODE_PROCESS_STARTED);

        Object expirationTime = verificationRepository.findByResetCode(resetCode);

        validateExpirationTime(expirationTime);

        log.info(VERIFY_CODE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updatePassword(ForgotPasswordRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = adminRepository.fetchAdminByEmail(requestDTO.getEmail());

        ForgotPasswordVerification forgotPasswordVerification = verificationRepository.findByAdminId(admin.getId());

        if (forgotPasswordVerification.getResetCode().equals(requestDTO.getVerificationToken())) {
            verify(requestDTO.getVerificationToken());
            updateAdminPassword(requestDTO, admin);
            updateForgotPasswordVerification(admin.getId());
        } else {
            log.error(INVALID_VERIFICATION_TOKEN_ERROR, requestDTO.getVerificationToken());
            throw new NoContentFoundException(INVALID_VERIFICATION_TOKEN);
        }

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));
    }

    private void updateAdminPassword(ForgotPasswordRequestDTO requestDTO, Admin admin) {
        admin.setPassword(BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt()));
        adminRepository.save(admin);
    }

    private void updateForgotPasswordVerification(Long adminId) {
        ForgotPasswordVerification forgotPasswordVerification = verificationRepository.findByAdminId(adminId);
        forgotPasswordVerification.setStatus(INACTIVE);
        save(forgotPasswordVerification);
    }

    private void validateExpirationTime(Object expirationTime) {
        if (((Date) expirationTime).before(new Date())) {
            log.error(RESET_CODE_EXPIRED);
            throw RESET_CODE_HAS_EXPIRED.get();
        }
    }

    public void save(ForgotPasswordVerification forgotPasswordVerification) {
        verificationRepository.save(forgotPasswordVerification);
    }

    private void validateAdmin(Admin admin, String email) {
        validateAdminStatus(admin, email);
        validateIfAdminIsActivated(admin, email);
    }

    private void validateAdminStatus(Admin admin, String username) {
        if (!admin.getStatus().equals(ACTIVE)) {
            log.error(ADMIN_NOT_ACTIVE_ERROR, username);
            throw new NoContentFoundException(String.format(ADMIN_NOT_ACTIVE, username), "username/email", username);
        }
    }

    private void validateIfAdminIsActivated(Admin admin, String username) {
        if (admin.getStatus().equals(NO)) {
            log.error(String.format(ACCOUNT_NOT_ACTIVATED_MESSAGE, username));
            throw new BadRequestException(
                    String.format(ACCOUNT_NOT_ACTIVATED_MESSAGE, username), ACCOUNT_NOT_ACTIVATED_DEBUG_MESSAGE);
        }
    }

    private Supplier<BadRequestException> RESET_CODE_HAS_EXPIRED = () ->
            new BadRequestException(RESET_CODE_EXPIRED);
}
