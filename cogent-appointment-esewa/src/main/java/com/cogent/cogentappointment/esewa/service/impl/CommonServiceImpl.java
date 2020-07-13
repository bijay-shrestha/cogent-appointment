package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.commons.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.common.DoctorSpecializationResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorMinResponseDTO;
import com.cogent.cogentappointment.esewa.repository.DoctorRepository;
import com.cogent.cogentappointment.esewa.repository.SpecializationRepository;
import com.cogent.cogentappointment.esewa.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.CommonLog.DOCTOR_SPECIALIZATION_INFO;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author smriti ON 05/02/2020
 */
@Service
@Transactional
@Slf4j
public class CommonServiceImpl implements CommonService {

    private final MinIOService minIOService;

    private final DoctorRepository doctorRepository;

    private final SpecializationRepository specializationRepository;

    public CommonServiceImpl(MinIOService minIOService,
                             DoctorRepository doctorRepository,
                             SpecializationRepository specializationRepository) {
        this.minIOService = minIOService;
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
    }

    @Override
    public DoctorSpecializationResponseDTO fetchDoctorSpecializationInfo(Long hospitalId) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, DOCTOR_SPECIALIZATION_INFO);

        List<DoctorMinResponseDTO> doctorInfo = doctorRepository.fetchDoctorMinInfo(hospitalId);

        doctorInfo.forEach(doctor -> {

            if (!isEmpty(doctor.getFileUri()) && !Objects.isNull(doctor.getFileUri())) {
                FileURLRequestDTO fileRequestDTO = FileURLRequestDTO.builder()
                        .fileName(doctor.getFileUri())
                        .build();
                doctor.setFileUri(minIOService.getPresignedObjectURL(fileRequestDTO));
            }
        });

        List<DropDownResponseDTO> specializationInfo =
                specializationRepository.fetchSpecializationByHospitalId(hospitalId);


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
