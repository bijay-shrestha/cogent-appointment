package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointmentTransfer.AppointmentTransferSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.AppointmentTransferLogResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.AppointmentTransferLog.previewDTO.AppointmentTransferPreviewResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableDates.DoctorDatesResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.ActualDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.OverrideDateAndTimeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.availableTime.WeekDayAndTimeDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentTransfer.charge.AppointmentChargeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Sauravi Thapa ON 5/6/20
 */
@Repository
@Qualifier("appointmentTransferRepositoryCustom")
public interface AppointmentTransferRepositoryCustom {
    List<DoctorDatesResponseDTO> getDatesByDoctorId(Long doctorId,
                                                    Long specializationId,
                                                    Long hospitalId);

    List<String> getDayOffDaysByRosterId(Long doctorDutyRosterId);

    WeekDayAndTimeDTO getWeekDaysByCode(Long doctorId,
                                        String code);

    List<DoctorDatesResponseDTO> getOverrideDatesByDoctorId(Long doctorId,
                                                            Long specializationId,
                                                            Long hospitalId);

    List<ActualDateAndTimeResponseDTO> getActualTimeByDoctorId(Long doctorId,
                                                               Long specializationId,
                                                               Long hospitalId);

    List<String> getUnavailableTimeByDateAndDoctorId(Long doctorId,
                                                     Long specializationId,
                                                     Date date,
                                                     Long hospitalId);

    AppointmentChargeResponseDTO getAppointmentChargeByDoctorId(Long doctorId,
                                                                Long hospitalId);

    List<OverrideDateAndTimeResponseDTO> getOverideRosterDateAndTime(Long doctorId, Long specializationId,
                                                                     Long hospitalId);

    AppointmentTransferLogResponseDTO getApptTransferredList(AppointmentTransferSearchRequestDTO requestDTO,
                                                             Pageable pageable);


    AppointmentTransferPreviewResponseDTO fetchAppointmentTransferDetailById(Long appointmentTransferId);
}
