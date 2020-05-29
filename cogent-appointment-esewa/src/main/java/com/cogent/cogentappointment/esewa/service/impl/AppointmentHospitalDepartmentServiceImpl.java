package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.repository.HospitalDeptDutyRosterOverrideRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalDeptDutyRosterRepository;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.esewa.constants.StatusConstants.NO;

/**
 * @author smriti on 28/05/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentServiceImpl implements AppointmentHospitalDepartmentService {

    private final HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository;

    private final HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository;

    public AppointmentHospitalDepartmentServiceImpl(
            HospitalDeptDutyRosterRepository hospitalDeptDutyRosterRepository,
            HospitalDeptDutyRosterOverrideRepository hospitalDeptDutyRosterOverrideRepository) {
        this.hospitalDeptDutyRosterRepository = hospitalDeptDutyRosterRepository;
        this.hospitalDeptDutyRosterOverrideRepository = hospitalDeptDutyRosterOverrideRepository;
    }

    @Override
    public List<AppointmentHospitalDeptCheckAvailabilityResponseDTO> fetchAvailableTimeSlots
            (AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        return null;
    }

    /*ASSUMING */
    private void fetchHospitalDeptDutyRoster(AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {

        List<HospitalDepartmentDutyRoster> dutyRosters =
                hospitalDeptDutyRosterRepository.fetchHospitalDeptDutyRoster(
                        requestDTO.getAppointmentDate(), requestDTO.getHospitalDepartmentId()
                );

        Character isRoomEnabled = dutyRosters.get(0).getIsRoomEnabled();

        dutyRosters.forEach(dutyRoster -> {

            if (dutyRoster.getIsRoomEnabled().equals(NO)) {

            }
        });

    }
}
