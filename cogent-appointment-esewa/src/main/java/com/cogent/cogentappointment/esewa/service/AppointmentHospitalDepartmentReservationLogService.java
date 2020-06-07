package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;

/**
 * @author smriti on 18/02/20
 */
public interface AppointmentHospitalDepartmentReservationLogService {

    Long saveAppointmentHospitalDeptReservationLog(AppointmentHospitalDeptFollowUpRequestDTO requestDTO);

    void deleteExpiredAppointmentHospitalDeptReservation();
}
