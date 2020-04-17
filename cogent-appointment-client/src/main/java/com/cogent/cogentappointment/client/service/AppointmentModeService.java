package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentMode.AppointmentModeMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentMode.AppointmentModeResponseDTO;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public interface AppointmentModeService {
    void save(AppointmentModeRequestDTO requestDTO);

    void update(AppointmentModeUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<AppointmentModeMinimalResponseDTO> search(AppointmentModeSearchRequestDTO searchRequestDTO,
                                                   Pageable pageable);

    AppointmentModeResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinAppointmentMode();

}
