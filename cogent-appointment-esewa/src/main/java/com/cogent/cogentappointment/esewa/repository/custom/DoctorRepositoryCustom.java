package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.doctor.DoctorMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 2019-09-29
 */
@Repository
@Qualifier("doctorRepositoryCustom")
public interface DoctorRepositoryCustom {

    List<DoctorMinResponseDTO> fetchDoctorMinInfo(Long hospitalId);

    Double fetchDoctorAppointmentFollowUpCharge(Long doctorId, Long hospitalId);

    Double fetchDoctorAppointmentCharge(Long doctorId, Long hospitalId);

}
