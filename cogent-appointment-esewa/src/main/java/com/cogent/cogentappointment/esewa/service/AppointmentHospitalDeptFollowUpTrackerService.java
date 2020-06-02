package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpResponseDTO;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentHospitalDeptFollowUpTrackerService {

    AppointmentHospitalDeptFollowUpResponseDTO fetchAppointmentHospitalDeptFollowUpDetails
            (AppointmentHospitalDeptFollowUpRequestDTO requestDTO);

    Long fetchByParentAppointmentId(Long parentAppointmentId);
}
