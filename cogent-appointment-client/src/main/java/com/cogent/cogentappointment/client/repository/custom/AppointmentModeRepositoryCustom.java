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

    List<DropDownResponseDTO> fetchActiveMinAppointmentMode();

}
