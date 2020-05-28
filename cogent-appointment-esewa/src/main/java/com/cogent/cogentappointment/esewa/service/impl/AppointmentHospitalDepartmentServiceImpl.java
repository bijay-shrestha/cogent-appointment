package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.service.AppointmentHospitalDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smriti on 28/05/20
 */
@Service
@Transactional
@Slf4j
public class AppointmentHospitalDepartmentServiceImpl implements AppointmentHospitalDepartmentService {



    @Override
    public List<AppointmentHospitalDeptCheckAvailabilityResponseDTO> fetchAvailableTimeSlots
            (AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO) {
        return null;
    }
}
