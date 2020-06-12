package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeResponseDTO;
import com.cogent.cogentappointment.admin.exception.BadRequestException;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.AppointmentModeRepository;
import com.cogent.cogentappointment.admin.service.AppointmentModeService;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AppointmentModeMessages.APPOINTMENT_MODE_NOT_EDITABLE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AppointmentModeMessages.APPOINTMENT_MODE_NOT_EDITABLE_DEBUG_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NAME_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentModeLog.APPOINTMENT_MODE;
import static com.cogent.cogentappointment.admin.log.constants.AppointmentModeLog.APPOINTMENT_MODE_NOT_EDITABLE_ERROR;
import static com.cogent.cogentappointment.admin.utils.AppointmentModeUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;

/**
 * @author Sauravi Thapa ON 4/17/20
 */

@Service
@Transactional
@Slf4j
public class AppointmentModeServiceImpl implements AppointmentModeService {

    private final AppointmentModeRepository repository;

    public AppointmentModeServiceImpl(AppointmentModeRepository repository) {
        this.repository = repository;
    }


    @Override
    public void save(AppointmentModeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, APPOINTMENT_MODE);

        List<Object[]> appointmentMode = repository.validateDuplicity(requestDTO);

        validateDuplicity(appointmentMode, requestDTO.getName(), requestDTO.getCode(), AppointmentMode.class.getSimpleName());

        saveAppointmentMode(parseToAppointmentMode(requestDTO));

        log.info(SAVING_PROCESS_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(AppointmentModeUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, APPOINTMENT_MODE);

        AppointmentMode appointmentMode = fetchAppointmentModeById(requestDTO.getId());

        if (appointmentMode.getIsEditable().equals('Y')) {

            List<Object[]> appointmentModes = repository.validateDuplicity(requestDTO);

            validateDuplicity(appointmentModes,
                    requestDTO.getName(),
                    requestDTO.getCode(),
                    AppointmentMode.class.getSimpleName());

            saveAppointmentMode(parseToUpdatedAppointmentMode(requestDTO, appointmentMode));

        } else {
            log.error(APPOINTMENT_MODE_NOT_EDITABLE_ERROR);
            throw new BadRequestException(APPOINTMENT_MODE_NOT_EDITABLE,APPOINTMENT_MODE_NOT_EDITABLE_DEBUG_MESSAGE);
        }

        log.info(UPDATING_PROCESS_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, APPOINTMENT_MODE);

        AppointmentMode appointmentMode = fetchAppointmentModeById(deleteRequestDTO.getId());

        saveAppointmentMode(parseToDeletedAppointmentMode(appointmentMode, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<AppointmentModeMinimalResponseDTO> search(AppointmentModeSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, APPOINTMENT_MODE);

        List<AppointmentModeMinimalResponseDTO> minimalResponseDTOS = repository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));

        return minimalResponseDTOS;
    }

    @Override
    public AppointmentModeResponseDTO fetchDetailsById(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, APPOINTMENT_MODE);

        AppointmentModeResponseDTO details = repository.fetchDetailsById(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));

        return details;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinAppointmentMode() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, APPOINTMENT_MODE);

        List<DropDownResponseDTO> minInfo = repository.fetchActiveMinAppointmentMode();

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, APPOINTMENT_MODE, getDifferenceBetweenTwoTime(startTime));

        return minInfo;
    }

    private void validateName(Long appointmentModeCount, String name) {
        if (appointmentModeCount.intValue() > 0) {
            log.error(NAME_DUPLICATION_ERROR, APPOINTMENT_MODE, name);
            throw new DataDuplicationException(
                    String.format(NAME_DUPLICATION_MESSAGE, AppointmentMode.class.getSimpleName(), name));
        }
    }

    private void saveAppointmentMode(AppointmentMode appointmentMode) {
        repository.save(appointmentMode);
    }

    private AppointmentMode fetchAppointmentModeById(Long id) {
        return repository.fetchAppointmentModeById(id)
                .orElseThrow(() -> APPOINTMENT_MODE_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> APPOINTMENT_MODE_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, APPOINTMENT_MODE, id);
        throw new NoContentFoundException(AppointmentMode.class, "id", id.toString());
    };
}
