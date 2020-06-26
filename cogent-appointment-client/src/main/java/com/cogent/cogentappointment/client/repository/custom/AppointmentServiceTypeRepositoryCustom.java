package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.ApptServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentServiceType.PrimaryAppointmentServiceTypeResponse;
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

    List<ApptServiceTypeDropDownResponseDTO> fetchSerivceTypeNameAndCodeList();

    PrimaryAppointmentServiceTypeResponse fetchAppointmentServiceTypeByHospital(Long loggedInHospitalId);
}
