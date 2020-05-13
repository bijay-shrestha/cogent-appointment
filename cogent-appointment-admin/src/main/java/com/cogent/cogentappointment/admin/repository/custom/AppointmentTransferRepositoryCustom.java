package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Repository
@Qualifier("appointmentTransferRepositoryCustom")
public interface AppointmentTransferRepositoryCustom {
    List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId, Long specializationId,Long hospitalId);

    List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId);

    WeekDayAndTimeDTO getWeekDaysByCode(Long doctorId, String code);

    List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId, Long specializationId,Long hospitalId);
//
//    List<ActualDateAndTimeResponseDTO> getActualTimeByDoctorId(Long doctorId, Long specializationId);
//
//    List<String> getUnavailableTimeByDateAndDoctorId(Long doctorId, Long specializationId, Date date);
//
//    AppointmentChargeResponseDTO getAppointmentChargeByDoctorId(Long doctorId);
//
//    List<OverrideDateAndTimeResponseDTO> getOverideRosterDateAndTime(Long doctorId, Long specializationId);
//
//    AppointmentTransferLogResponseDTO getFinalAppTransferredInfo(AppointmentTransferSearchRequestDTO requestDTO, Pageable pageable);
}
