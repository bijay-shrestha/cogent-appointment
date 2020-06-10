package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.StatusResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO;

/**
 * @author smriti on 28/05/20
 */
public interface AppointmentHospitalDepartmentService {

    AppointmentHospitalDeptCheckAvailabilityResponseDTO fetchAvailableTimeSlots
            (AppointmentHospitalDeptCheckAvailabilityRequestDTO requestDTO);

    AppointmentHospitalDeptCheckAvailabilityRoomWiseResponseDTO fetchAvailableTimeSlotsRoomWise
            (AppointmentHospitalDeptCheckAvailabilityRoomWiseRequestDTO requestDTO);

    StatusResponseDTO cancelRegistration(Long appointmentReservationId);
}
