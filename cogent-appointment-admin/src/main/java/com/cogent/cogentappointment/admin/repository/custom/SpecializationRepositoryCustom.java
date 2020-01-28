package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.specialization.SpecializationResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
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

    Long fetchSpecializationByName(String name);

    Long fetchSpecializationByIdAndName(Long id, String name);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveSpecializationForDropDown();

    SpecializationResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId);
}
