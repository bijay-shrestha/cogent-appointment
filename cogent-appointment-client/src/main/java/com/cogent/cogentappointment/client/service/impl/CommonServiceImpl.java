package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.common.DoctorSpecializationResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorMinResponseDTO;
import com.cogent.cogentappointment.client.repository.DoctorRepository;
import com.cogent.cogentappointment.client.repository.SpecializationRepository;
import com.cogent.cogentappointment.client.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.CommonLog.DOCTOR_SPECIALIZATION_INFO;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author smriti ON 05/02/2020
 */
@Service
@Transactional
@Slf4j
public class CommonServiceImpl implements CommonService {

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    public CommonServiceImpl(DoctorRepository doctorRepository,
                             SpecializationRepository specializationRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }

    @Override
    public DoctorSpecializationResponseDTO fetchDoctorSpecializationInfo(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_SPECIALIZATION_INFO);

        List<DoctorMinResponseDTO> doctorInfo = doctorRepository.fetchDoctorMinInfo(hospitalId);

        List<DropDownResponseDTO> specializationInfo =
                specializationRepository.fetchActiveSpecializationByHospitalId(hospitalId);

        DoctorSpecializationResponseDTO commonInfo = DoctorSpecializationResponseDTO.builder()
                .doctorInfo(doctorInfo)
                .specializationInfo(specializationInfo)
                .responseStatus(OK)
                .responseCode(OK.value())
                .build();

        log.info(FETCHING_PROCESS_COMPLETED, DOCTOR_SPECIALIZATION_INFO,
                getDifferenceBetweenTwoTime(startTime));

        return commonInfo;
    }
}
