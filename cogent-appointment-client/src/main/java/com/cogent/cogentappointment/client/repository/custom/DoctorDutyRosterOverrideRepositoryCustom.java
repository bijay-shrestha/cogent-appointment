package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.AppointmentStatusRequestDTO;
import com.cogent.cogentappointment.client.dto.request.doctorDutyRoster.DoctorDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime.DoctorDutyRosterOverrideAppointmentDate;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.RosterDetailsForStatus;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterOverride;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Qualifier("doctorDutyRosterOverrideRepositoryCustom")
public interface DoctorDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCount(Long doctorId, Long specializationId, Date fromDate, Date toDate);

    Long fetchOverrideCount(Long doctorDutyRosterOverrideId, Long doctorId,
                            Long specializationId, Date fromDate, Date toDate);


    List<DoctorDutyRosterOverride> fetchDoctorDutyRosterOverrides(
            List<DoctorDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS);

    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterOverrideStatus
            (AppointmentStatusRequestDTO requestDTO, Long hospitalId);

    RosterDetailsForStatus fetchOverrideRosterDetails(RosterDetailsForStatus rosterDetailsForStatus,
                                                      Date appointmentDate);

}
