package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDepartmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpResponseDTO;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentHospitalDeptFollowUpTrackerService {

    AppointmentHospitalDeptFollowUpResponseDTO fetchAppointmentHospitalDeptFollowUpDetails
            (AppointmentHospitalDepartmentFollowUpRequestDTO requestDTO);

    Long fetchByParentAppointmentId(Long parentAppointmentId);
}
