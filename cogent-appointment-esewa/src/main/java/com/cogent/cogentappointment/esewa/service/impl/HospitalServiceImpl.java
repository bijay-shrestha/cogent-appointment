package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.commons.dto.request.file.FileURLRequestDTO;
import com.cogent.cogentappointment.commons.service.MinIOService;
import com.cogent.cogentappointment.esewa.dto.request.hospital.HospitalMinSearchRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalAppointmentServiceTypeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospital.HospitalMinResponseDTOWithStatus;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.HospitalRepository;
import com.cogent.cogentappointment.esewa.service.HospitalService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalLog.HOSPITAL_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.utils.HospitalUtils.parseToHospitalMinResponseDTOWithStatus;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @author smriti ON 12/01/2020
 */
@Service
@Transactional
@Slf4j
public class HospitalServiceImpl implements HospitalService {

    private final MinIOService minIOService;

    private final HospitalRepository hospitalRepository;

    public HospitalServiceImpl(MinIOService minIOService, HospitalRepository hospitalRepository) {
        this.minIOService = minIOService;
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

        responseDTO.forEach((HospitalMinResponseDTO hospital) -> {

            if (!isEmpty(hospital.getHospitalLogo()) && !Objects.isNull(hospital.getHospitalLogo())) {
                FileURLRequestDTO fileRequestDTO = new FileURLRequestDTO();
                fileRequestDTO.setFileName(hospital.getHospitalLogo());
                hospital.setHospitalLogo(minIOService.getPresignedObjectURL(fileRequestDTO));
            }

            if (!isEmpty(hospital.getHospitalBanner()) && !Objects.isNull(hospital.getHospitalBanner())) {
                FileURLRequestDTO fileRequestDTO = new FileURLRequestDTO();
                fileRequestDTO.setFileName(hospital.getHospitalBanner());
                hospital.setHospitalBanner(minIOService.getPresignedObjectURL(fileRequestDTO));
            }
        });

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL, getDifferenceBetweenTwoTime(startTime));

        return parseToHospitalMinResponseDTOWithStatus(responseDTO);
    }

    @Override
    public List<HospitalAppointmentServiceTypeResponseDTO> fetchHospitalAppointmentServiceType(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL_APPOINTMENT_SERVICE_TYPE);

        List<HospitalAppointmentServiceTypeResponseDTO> hospitalAppointmentServiceType =
                hospitalRepository.fetchHospitalAppointmentServiceType(hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_APPOINTMENT_SERVICE_TYPE,
                getDifferenceBetweenTwoTime(startTime));

        return hospitalAppointmentServiceType;
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };
}
