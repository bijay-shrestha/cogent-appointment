package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */

@Repository
@Qualifier("appointmentModeRepositoryCustom")
public interface AppointmentModeRepositoryCustom {
    Long validateDuplicity(String name);

    Long validateDuplicity(Long id, String name);

//    List<UniversityMinimalResponseDTO> search(UniversitySearchRequestDTO searchRequestDTO,
//                                              Pageable pageable);
//
//    UniversityResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinUniversity();

}
