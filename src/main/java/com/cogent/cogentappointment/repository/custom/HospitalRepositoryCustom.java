package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.dto.response.hospital.HospitalResponseDTO;
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

    Long fetchHospitalByName(String name);

    Long findHospitalByIdAndName(Long id, String name);

    List<HospitalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO, Pageable pageable);

    List<DropDownResponseDTO> fetchActiveHospitalForDropDown();

    HospitalResponseDTO fetchDetailsById(Long id);
}
