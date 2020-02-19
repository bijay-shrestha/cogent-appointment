package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentFollowUpRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentFollowUpResponseDTO;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;

/**
 * @author smriti on 16/02/20
 */
public interface AppointmentFollowUpTrackerService {

    AppointmentFollowUpResponseDTO fetchAppointmentFollowUpDetails(AppointmentFollowUpRequestDTO requestDTO);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();

    void save(Long parentAppointmentId, String parentAppointmentNumber, Hospital hospital,
              Doctor doctor, Specialization specialization, Patient patient);

}
