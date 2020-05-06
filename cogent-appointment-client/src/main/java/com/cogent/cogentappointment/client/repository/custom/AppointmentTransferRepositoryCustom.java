package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.DoctorDatesResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Repository
@Qualifier("appointmentTransferRepositoryCustom")
public interface AppointmentTransferRepositoryCustom {
    List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId);

    List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId);

    List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId);
}
