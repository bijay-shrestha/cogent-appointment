package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentDoctorInfoRepositoryCustom")
public interface HospitalDepartmentDoctorInfoRepositoryCustom {

    List<DoctorDropdownDTO> fetchAssignedHospitalDepartmentDoctor(Long hospitalDepartmentId);

}
