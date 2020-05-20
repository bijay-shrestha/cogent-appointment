package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentRepositoryCustom")
public interface HospitalDepartmentRepositoryCustom {
    List<Object[]> validateDuplicity(HospitalDepartmentRequestDTO requestDTO, Long hospitalId);

    List<Object[]> validateDuplicity(HospitalDepartmentUpdateRequestDTO requestDTO, Long hospitalId);

    Optional<List<DropDownResponseDTO>> fetchMinHospitalDepartment(Long hospitalId);

    Optional<List<DropDownResponseDTO>> fetchActiveMinHospitalDepartment(Long hospitalId);
}
