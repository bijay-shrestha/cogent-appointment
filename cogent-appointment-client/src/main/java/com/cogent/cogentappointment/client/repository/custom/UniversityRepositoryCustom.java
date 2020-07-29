package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.university.UniversityResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
@Repository
@Qualifier("universityRepositoryCustom")
public interface UniversityRepositoryCustom {

    Long validateDuplicity(String name);

    Long validateDuplicity(Long id, String name);

    List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
                                              Pageable pageable);

    UniversityResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinUniversity();

    List<DropDownResponseDTO> fetchMinUniversity();
}
