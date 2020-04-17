package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentMode.AppointmentModeMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentMode.AppointmentModeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
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

    List<AppointmentModeMinimalResponseDTO> search(AppointmentModeSearchRequestDTO searchRequestDTO,
                                                   Pageable pageable);

    AppointmentModeResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinAppointmentMode();

}
