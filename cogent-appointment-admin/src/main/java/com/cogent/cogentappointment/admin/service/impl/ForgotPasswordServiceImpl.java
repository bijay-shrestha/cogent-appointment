package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants;
import com.cogent.cogentappointment.admin.constants.StatusConstants;
import com.cogent.cogentappointment.admin.dto.request.email.EmailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.forgotPassword.ForgotPasswordRequestDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.property.ExpirationTimeProperties;
import com.cogent.cogentappointment.admin.repository.AdminRepository;
import com.cogent.cogentappointment.admin.repository.ForgotPasswordRepository;
import com.cogent.cogentappointment.admin.service.EmailService;
import com.cogent.cogentappointment.admin.service.ForgotPasswordService;
import com.cogent.cogentappointment.admin.utils.ForgotPasswordUtils;
import com.cogent.cogentappointment.admin.utils.commons.DateUtils;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.ForgotPasswordVerification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.log.constants.AdminLog.*;
import static java.util.Objects.isNull;

/**
 * @author smriti on 2019-09-20
 */
@Service
@Transactional
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final AdminRepository adminRepository;

    private final ExpirationTimeProperties expirationTimeProperties;

    private final ForgotPasswordRepository verificationRepository;

    private final EmailService emailService;

    public ForgotPasswordServiceImpl(AdminRepository adminRepository,
                                     ExpirationTimeProperties expirationTimeProperties,
                                     ForgotPasswordRepository verificationRepository,
                                     EmailService emailService) {
        this.adminRepository = adminRepository;
        this.expirationTimeProperties = expirationTimeProperties;
        this.verificationRepository = verificationRepository;
        this.emailService = emailService;
    }

    @Override
    public void forgotPassword(String username) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(FORGOT_PASSWORD_PROCESS_STARTED);

        Admin admin = adminRepository.fetchAdminByUsernameOrEmail(username);

        validateAdmin(admin, username);

        ForgotPasswordVerification forgotPasswordVerification = verificationRepository.findByAdminId(admin.getId());

        ForgotPasswordUtils.convertToForgotPasswordVerification(admin,
                expirationTimeProperties.getForgotPassword(),
                isNull(forgotPasswordVerification) ? new ForgotPasswordVerification() : forgotPasswordVerification);

        save(forgotPasswordVerification);

        EmailRequestDTO emailRequestDTO = ForgotPasswordUtils.parseToEmailRequestDTO(admin.getEmail(),
                admin.getUsername(), forgotPasswordVerification.getResetCode());

        emailService.sendEmail(emailRequestDTO);

        log.info(FORGOT_PASSWORD_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void verify(String resetCode) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(VERIFY_CODE_PROCESS_STARTED);

        Object expirationTime = verificationRepository.findByResetCode(resetCode);
        validateExpirationTime(expirationTime);

        log.info(VERIFY_CODE_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void updatePassword(ForgotPasswordRequestDTO requestDTO) {
        Long startTime = DateUtils.getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PASSWORD_PROCESS_STARTED);

        Admin admin = adminRepository.fetchAdminByUsernameOrEmail(requestDTO.getUsername());
        updateAdminPassword(requestDTO, admin);
        updateForgotPasswordVerification(admin.getId());

        log.info(UPDATING_PASSWORD_PROCESS_COMPLETED, DateUtils.getDifferenceBetweenTwoTime(startTime));
    }

    public void updateAdminPassword(ForgotPasswordRequestDTO requestDTO, Admin admin) {
        admin.setPassword(BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt()));
        adminRepository.save(admin);
    }

    public void updateForgotPasswordVerification(Long adminId) {
        ForgotPasswordVerification forgotPasswordVerification = verificationRepository.findByAdminId(adminId);
        forgotPasswordVerification.setStatus(StatusConstants.INACTIVE);
        save(forgotPasswordVerification);
    }

    private void validateExpirationTime(Object expirationTime) {
        if (((Date) expirationTime).before(new Date())) throw RESET_CODE_HAS_EXPIRED.get();
    }

    public void save(ForgotPasswordVerification forgotPasswordVerification) {
        verificationRepository.save(forgotPasswordVerification);
    }

    private void validateAdmin(Admin admin, String username) {
        if (!admin.getStatus().equals(StatusConstants.ACTIVE))
            throw new NoContentFoundException(String.format(ErrorMessageConstants.AdminServiceMessages.ADMIN_NOT_ACTIVE, username), "username/email", username);
    }

    private Supplier<BadRequestException> RESET_CODE_HAS_EXPIRED = () ->
            new BadRequestException(ErrorMessageConstants.ForgotPasswordMessages.RESET_CODE_EXPIRED);
}
