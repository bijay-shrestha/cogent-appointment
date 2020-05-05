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

    List<Object[]> validateDuplicity(String name, String code, Long hospitalId);

    List<Object[]> validateDuplicity(Long id, String name, String code, Long hospitalId);

    List<SpecializationMinimalResponseDTO> search(SpecializationSearchRequestDTO searchRequestDTO,
                                                  Long hospitalId,
                                                  Pageable pageable);

    List<DropDownResponseDTO> fetchActiveMinSpecialization(Long hospitalId);

    List<DropDownResponseDTO> fetchMinSpecialization(Long hospitalId);

    SpecializationResponseDTO fetchDetailsById(Long id,Long hospitalId);

    List<DropDownResponseDTO> fetchSpecializationByDoctorId(Long doctorId,Long hospitalId);

    List<DropDownResponseDTO> fetchActiveSpecializationByHospitalId(Long hospitalId);

}
