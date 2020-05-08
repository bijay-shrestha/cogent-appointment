package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Repository
@Qualifier("appointmentTransferRepositoryCustom")
public interface AppointmentTransferRepositoryCustom {
    List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId,Long specializationId);

    List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId);

    WeekDayAndTimeDTO getWeekDaysByCode(Long doctorId,String code);

    List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId,Long specializationId);

    List<ActualDateAndTimeResponseDTO> getActualTimeByDoctorId(Long doctorId,Long specializationId);

    List<OverrideDateAndTimeResponseDTO> getOverrideTimeByRosterId(Long doctorDutyRosterId);

    List<String> getUnavailableTimeByDateAndDoctorId(Long doctorId,Long specializationId,Date date);

    AppointmentChargeResponseDTO getAppointmentChargeByDoctorId(Long doctorId);

    List<OverrideDateAndTimeResponseDTO> getOverideRosterDateAndTime(Long doctorId, Long specializationId);
}
