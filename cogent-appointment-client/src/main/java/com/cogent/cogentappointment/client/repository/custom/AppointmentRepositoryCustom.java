package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.request.appointment.AppointmentPendingSearchDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedDateResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.AppointmentPendingResponseDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appointmentQueue.AppointmentQueueSearchDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRepositoryCustom")
public interface AppointmentRepositoryCustom {

    Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                     Long doctorId, Long specializationId);

    String generateAppointmentNumber(String nepaliCreatedDate, Long hospitalId);

    List<AppointmentPendingResponseDTO> fetchPendingAppointments(AppointmentPendingSearchDTO searchDTO);

    Double calculateRefundAmount(Long appointmentId);

    List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(AppointmentCheckAvailabilityRequestDTO requestDTO);


    List<AppointmentBookedDateResponseDTO> fetchBookedAppointmentDates(Date fromDate,
                                                                       Date toDate,
                                                                       Long doctorId,
                                                                       Long specializationId);

    Long fetchBookedAppointmentCount(Date fromDate, Date toDate, Long doctorId, Long specializationId);

    Long countRegisteredPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countNewPatientByHospitalId(DashBoardRequestDTO dashBoardRequestDTO);

    Long countOverAllAppointment(DashBoardRequestDTO dashBoardRequestDTO);

    AppointmentQueueSearchDTO fetchTodayAppointmentQueue(AppointmentQueueRequestDTO appointmentQueueRequestDTO,
                                                         Long hospitalId,
                                                         Pageable pageable);

    Map<String, List<AppointmentQueueDTO>> fetchTodayAppointmentQueueByTime(
            AppointmentQueueRequestDTO appointmentQueueRequestDTO,
            Long hospitalId,
            Pageable pageable);
}
