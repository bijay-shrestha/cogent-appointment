package com.cogent.cogentappointment.esewa.service;

import com.cogent.cogentappointment.esewa.dto.request.appointment.followup.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTOWithStatus;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentFollowUpTrackerService {

    AppointmentFollowUpResponseDTOWithStatus fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO);

    Long fetchByParentAppointmentId(Long parentAppointmentId);
}
