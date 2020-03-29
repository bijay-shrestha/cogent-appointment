package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinResponseDTOWithStatus;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.utils.HospitalUtils.parseToHospitalMinResponseDTOWithStatus;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti ON 12/01/2020
 */
@Service
@Transactional
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalServiceImpl(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public Hospital fetchActiveHospital(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL);

        Hospital hospital = hospitalRepository.findActiveHospitalById(id)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id));

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return hospital;
    }

    @Override
    public HospitalMinResponseDTOWithStatus fetchMinDetails(HospitalMinSearchRequestDTO searchRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL);

        List<HospitalMinResponseDTO> responseDTO = hospitalRepository.fetchMinDetails(searchRequestDTO);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return parseToHospitalMinResponseDTOWithStatus(responseDTO);
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error("Hospital with id : {} not found", id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };
}
