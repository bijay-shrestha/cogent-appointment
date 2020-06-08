package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDoctorInfo;
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

    List<HospitalDepartmentDoctorInfo> fetchActiveHospitalDepartmentDoctorInfo(List<Long> hospitalDepartmentDoctorInfoIds);
}
