package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalFollowUpResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Qualifier("hospitalRepositoryCustom")
public interface HospitalRepositoryCustom {

    Integer fetchHospitalFollowUpIntervalDays(Long hospitalId);

    Integer fetchHospitalFollowUpCount(Long hospitalId);

    HospitalFollowUpResponseDTO fetchFollowUpDetails(Long hospitalId);

    List<AppointmentServiceTypeDropDownResponseDTO> fetchAssignedAppointmentServiceType(Long hospitalId);
}
