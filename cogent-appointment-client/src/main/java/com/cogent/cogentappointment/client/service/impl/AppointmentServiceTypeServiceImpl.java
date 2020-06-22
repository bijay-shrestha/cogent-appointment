package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.ApptServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AppointmentServiceTypeRepository;
import com.cogent.cogentappointment.client.service.AppointmentServiceTypeService;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 26/05/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentServiceTypeServiceImpl implements AppointmentServiceTypeService {

    private final AppointmentServiceTypeRepository appointmentServiceTypeRepository;

    public AppointmentServiceTypeServiceImpl(AppointmentServiceTypeRepository appointmentServiceTypeRepository) {
        this.appointmentServiceTypeRepository = appointmentServiceTypeRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinInfo() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, APPOINTMENT_SERVICE_TYPE);

        List<DropDownResponseDTO> minInfo = appointmentServiceTypeRepository.fetchActiveMinInfo();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, APPOINTMENT_SERVICE_TYPE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    @Override
    public AppointmentServiceType fetchActiveById(Long id) {
        return appointmentServiceTypeRepository.fetchActiveById(id)
                .orElseThrow(() -> NO_APPOINTMENT_SERVICE_TYPE_FOUND.apply(id));
    }

    @Override
    public List<ApptServiceTypeDropDownResponseDTO> fetchSerivceTypeNameAndCodeList() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, APPOINTMENT_SERVICE_TYPE);

        List<ApptServiceTypeDropDownResponseDTO> minInfo = appointmentServiceTypeRepository.fetchSerivceTypeNameAndCodeList();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, APPOINTMENT_SERVICE_TYPE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    private Function<Long, NoContentFoundException> NO_APPOINTMENT_SERVICE_TYPE_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_SERVICE_TYPE, id);
        throw new NoContentFoundException(AppointmentServiceType.class, "id", id.toString());
    };
}
