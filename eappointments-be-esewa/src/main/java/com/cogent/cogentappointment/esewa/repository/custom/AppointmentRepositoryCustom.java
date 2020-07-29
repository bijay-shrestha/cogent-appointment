package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility.AppointmentCheckAvailabilityRequestDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentHistorySearchDTO;
import com.cogent.cogentappointment.esewa.dto.request.appointment.history.AppointmentSearchDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty.AppointmentBookedTimeResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentDetailResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentMinResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.appointment.history.AppointmentResponseWithStatusDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentRepositoryCustom")
public interface AppointmentRepositoryCustom {

    Long validateIfAppointmentExists(Date appointmentDate, String appointmentTime,
                                     Long doctorId, Long specializationId);

    String generateAppointmentNumber(String nepaliCreatedDate, Long hospitalId, String hospitalCode);

    List<AppointmentMinResponseDTO> fetchPendingAppointments(AppointmentHistorySearchDTO searchDTO);

    AppointmentDetailResponseDTO fetchAppointmentDetails(Long appointmentId);

    List<AppointmentMinResponseDTO> fetchAppointmentHistory(AppointmentHistorySearchDTO searchDTO);

    AppointmentResponseWithStatusDTO searchAppointmentsForSelf(AppointmentSearchDTO searchDTO,
                                                               Pageable pageable);

    AppointmentResponseWithStatusDTO searchAppointmentsForOthers(
            AppointmentSearchDTO searchDTO,
            Pageable pageable);

    List<AppointmentBookedTimeResponseDTO> fetchBookedAppointments(AppointmentCheckAvailabilityRequestDTO requestDTO);

    Double calculateRefundAmount(Long appointmentId);

    /*department-wise*/
    List<AppointmentBookedTimeResponseDTO> fetchBookedAppointmentDeptWise(
            Date appointmentDate, Long hospitalDepartmentId, Long hospitalDepartmentRoomInfoId);

    Long validateIfAppointmentExistsDeptWise(Date appointmentDate, String appointmentTime,
                                             Long hospitalDepartmentId, Long hospitalDepartmentRoomInfoId);

}