package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.appointmentMode.AppointmentModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentMode.AppointmentModeResponseDTO;
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
    List<Object[]> validateDuplicity(AppointmentModeRequestDTO requestDTO);

    List<Object[]> validateDuplicity(AppointmentModeUpdateRequestDTO requestDTO);

    List<AppointmentModeMinimalResponseDTO> search(AppointmentModeSearchRequestDTO searchRequestDTO,
                                                   Pageable pageable);

    AppointmentModeResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinAppointmentMode();

}
