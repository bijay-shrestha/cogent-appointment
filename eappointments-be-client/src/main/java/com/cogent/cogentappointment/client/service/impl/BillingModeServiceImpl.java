package com.cogent.cogentappointment.client.service.impl;


import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.BillingModeRepository;
import com.cogent.cogentappointment.client.service.BillingModeService;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.log.constants.BillingModeLog.BILLING_MODE;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;


/**
 * @author Sauravi Thapa ON 4/17/20
 */

@Service
@Transactional
@Slf4j
public class BillingModeServiceImpl implements BillingModeService {

    private final BillingModeRepository billingModeRepository;

    public BillingModeServiceImpl(BillingModeRepository billingModeRepository) {
        this.billingModeRepository = billingModeRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinBillingMode() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository
                .fetchActiveMinBillingModeByHospitalId(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinBillingMode() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, BILLING_MODE);

        List<DropDownResponseDTO> minInfo = billingModeRepository
                .fetchMinBillingModeByHospitalId(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, BILLING_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }


}
