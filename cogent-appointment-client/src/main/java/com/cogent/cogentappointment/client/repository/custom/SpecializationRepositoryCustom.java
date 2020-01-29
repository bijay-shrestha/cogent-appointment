package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.specialization.SpecializationSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.specialization.SpecializationResponseDTO;
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

    Long validateDuplicity(String name, Long hospitalId);

    Long validateDuplicity(Long id, String name, Long hospitalId);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveSpecializationForDropDown();

    SpecializationResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId);

    List<DropDownResponseDTO> fetchSpecializationByHospitalId(Long hospitalId);
}
