package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;

/**
 * @author smriti on 18/11/2019
 */
public interface AppointmentFollowUpTrackerService {

//    List<FollowUpTrackerResponseDTO> fetchMinimalFollowUpTracker(FollowUpTrackerSearchRequestDTO requestDTO);

    void save(Long parentAppointmentId, String parentAppointmentNumber, Hospital hospital,
              Doctor doctor, Specialization specialization, Patient patient);

    void updateFollowUpTracker(Long parentAppointmentId);

    void updateFollowUpTrackerStatus();
}
