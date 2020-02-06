package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversitySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.university.UniversityUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.university.UniversityMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.university.UniversityResponseDTO;
import com.cogent.cogentappointment.persistence.model.University;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 08/11/2019
 */
public interface UniversityService {

    void save(UniversityRequestDTO requestDTO);

    void update(UniversityUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
                                              Pageable pageable);

    UniversityResponseDTO fetchDetailsById(Long id);


    List<DropDownResponseDTO> fetchActiveUniversity();

    University fetchUniversityById(Long id);
}
