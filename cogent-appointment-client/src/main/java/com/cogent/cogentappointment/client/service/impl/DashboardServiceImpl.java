package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.dashboard.GenerateRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.GenerateRevenueResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentTransactionInfoRepository;
import com.cogent.cogentappointment.client.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.DashboardLog.DASHBOARD;
import static com.cogent.cogentappointment.client.utils.DashboardUtils.parseToGenerateRevenueResponseDTO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.MathUtils.calculatePercenatge;
import static com.cogent.cogentappointment.client.utils.commons.TimeConverterUtils.dateDifference;

/**
 * @author Sauravi Thapa २०/२/१०
 */

@Service
@Slf4j
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private AppointmentTransactionInfoRepository appointmentTransactionInfoRepository;

    public DashboardServiceImpl(AppointmentTransactionInfoRepository appointmentTransactionInfoRepository) {
        this.appointmentTransactionInfoRepository = appointmentTransactionInfoRepository;
    }

    @Override
    public GenerateRevenueResponseDTO getRevenueGeneratedDetail(GenerateRevenueRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DASHBOARD);

        Double currentTransaction = appointmentTransactionInfoRepository.getRevenueByDates(requestDTO.getCurrentToDate(),
                requestDTO.getCurrentFromDate());

        Double previousTransaction=appointmentTransactionInfoRepository.getRevenueByDates(requestDTO.getPreviousToDate(),
                requestDTO.getPreviousFromDate());

        Double growthPercent=calculatePercenatge(currentTransaction,previousTransaction);

        Character revenueType=dateDifference(requestDTO.getCurrentToDate(),
                requestDTO.getCurrentFromDate());

        GenerateRevenueResponseDTO responseDTO=parseToGenerateRevenueResponseDTO(currentTransaction,growthPercent,revenueType);


        log.info(FETCHING_PROCESS_COMPLETED, DASHBOARD, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }
}
