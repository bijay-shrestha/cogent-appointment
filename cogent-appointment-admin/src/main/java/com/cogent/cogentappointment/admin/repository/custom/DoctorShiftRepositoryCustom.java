package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorShiftMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 10/05/20
 */
@Repository
@Qualifier("doctorShiftRepositoryCustom")
public interface DoctorShiftRepositoryCustom {

    List<DoctorShiftMinResponseDTO> fetchAssignedDoctorShifts(Long doctorId);

    List<Long> fetchActiveAssignedDoctorShiftIds(Long doctorId);

    Long validateDoctorShiftCount(List<Long> shiftIds, Long doctorId);
}
