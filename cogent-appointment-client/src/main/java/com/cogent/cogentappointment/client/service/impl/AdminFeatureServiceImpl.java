package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.adminFeature.AdminFeatureRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AdminFeatureRepository;
import com.cogent.cogentappointment.client.service.AdminFeatureService;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.AdminFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.UPDATING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.UPDATING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AdminFeatureLog.ADMIN_FEATURE_LOG;
import static com.cogent.cogentappointment.client.utils.AdminFeatureUtils.parseToAdminFeature;
import static com.cogent.cogentappointment.client.utils.AdminFeatureUtils.updateAdminFeatureFlag;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInAdminUsername;

/**
 * @author smriti on 18/04/20
 */
@Service
@Transactional
@Slf4j
public class AdminFeatureServiceImpl implements AdminFeatureService {

    private final AdminFeatureRepository adminFeatureRepository;

    public AdminFeatureServiceImpl(AdminFeatureRepository adminFeatureRepository) {
        this.adminFeatureRepository = adminFeatureRepository;
    }

    @Override
    public void save(Admin admin) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_FEATURE_LOG);

        AdminFeature adminFeature = parseToAdminFeature(admin);

        adminFeatureRepository.save(adminFeature);

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN_FEATURE_LOG, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(AdminFeatureRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ADMIN_FEATURE_LOG);

        String username = getLoggedInAdminUsername();

        AdminFeature adminFeature = adminFeatureRepository.findAdminFeatureByAdminUsername(username)
                .orElseThrow(() -> new NoContentFoundException(AdminFeature.class));

        updateAdminFeatureFlag(adminFeature, requestDTO.getIsSideBarCollapse());

        log.info(UPDATING_PROCESS_COMPLETED, ADMIN_FEATURE_LOG, getDifferenceBetweenTwoTime(startTime));
    }
}
