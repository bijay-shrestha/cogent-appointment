package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.AppointmentServiceTypeRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentServiceTypeService;
import com.cogent.cogentappointment.persistence.model.AppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.AppointmentServiceTypeLog.APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

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

        log.info(FETCHING_PROCESS_STARTED, APPOINTMENT_SERVICE_TYPE);

        List<DropDownResponseDTO> minInfo = appointmentServiceTypeRepository.fetchActiveMinInfo();

        log.info(FETCHING_PROCESS_COMPLETED, APPOINTMENT_SERVICE_TYPE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    private Function<Long, NoContentFoundException> NO_APPOINTMENT_SERVICE_TYPE_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_SERVICE_TYPE, id);
        throw new NoContentFoundException(AppointmentServiceType.class, "id", id.toString());
    };
}
