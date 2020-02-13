package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.Patient;
import com.cogent.cogentappointment.persistence.model.Specialization;

/**
 * @author smriti on 18/11/2019
 */
public interface AppointmentFollowUpTrackerService {

//    List<FollowUpTrackerResponseDTO> fetchMinimalFollowUpTracker(FollowUpTrackerSearchRequestDTO requestDTO);

    void save(String parentAppointmentNumber,
              Long hospitalId,
              Doctor doctor,
              Specialization specialization,
              Patient patient);

//    void updateNumberOfFollowupsInFollowUpTracker(String parentAppointmentNumber,
//                                                  Long doctorId,
//                                                  Long patientId);

    void updateFollowUpTrackerStatus();
}
