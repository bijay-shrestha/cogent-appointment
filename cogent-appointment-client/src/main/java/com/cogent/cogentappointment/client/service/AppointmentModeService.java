package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public interface AppointmentModeService {
//    void save(UniversityRequestDTO requestDTO);
//
//    void update(UniversityUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

//    List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
//                                              Pageable pageable);
//
//    UniversityResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinUniversity();

    AppointmentMode findActiveUniversityById(Long id);
}
