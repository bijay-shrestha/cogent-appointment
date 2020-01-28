package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospital.HospitalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Qualifier("hospitalRepositoryCustom")
public interface HospitalRepositoryCustom {

    List<Object[]> validateHospitalDuplicity(String name, String code);

    List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String code);

    List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO, Pageable pageable);

    List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown();

    HospitalResponseDTO fetchDetailsById(Long id);
}
