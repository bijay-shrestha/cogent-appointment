package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 26/05/20
 */
@Repository
@Qualifier("appointmentServiceTypeRepositoryCustom")
public interface AppointmentServiceTypeRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveMinInfo();
}
