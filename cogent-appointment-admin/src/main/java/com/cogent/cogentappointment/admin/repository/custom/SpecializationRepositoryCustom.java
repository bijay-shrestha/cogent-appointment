package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 2019-08-11
 */
@Repository
@Qualifier("specializationRepositoryCustom")
public interface SpecializationRepositoryCustom {

    List<Object[]> validateDuplicity(String name, String code, Long hospitalId);

    List<Object[]> validateDuplicity(Long id, String name, String code, Long hospitalId);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveSpecializationForDropDown();

    List<DropDownResponseDTO> fetchSpecializationForDropDown();

    SpecializationResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveSpecializationByDoctorId(Long doctorId);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId);

    List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId);
}
