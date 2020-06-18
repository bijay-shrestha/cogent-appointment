package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.RosterDetailsForStatus;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterOverrideRepositoryCustom")
public interface HospitalDeptDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCountWithoutRoom(Long hospitalDepartmentId, Date fromDate, Date toDate);

    Long fetchOverrideCountWithRoom(Long hospitalDepartmentId, Date fromDate,
                                    Date toDate, Long hospitalDepartmentRoomInfoId);

    Long fetchOverrideCountWithoutRoomExceptCurrentId(Long hospitalDepartmentId,
                                                      Date fromDate, Date toDate, Long rosterOverrideId);

    Long fetchOverrideCountWithRoomExceptCurrentId(Long hospitalDepartmentId, Date fromDate, Date toDate,
                                                   Long hospitalDepartmentRoomInfoId, Long rosterOverrideId);

    void updateOverrideStatus(Long hddRosterId);

    void updateOverrideRoomInfo(Long hddRosterId, Long hospitalDepartmentRoomInfoId);

    List<HospitalDepartmentDutyRosterOverride> fetchOverrideList(
            List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS);

    List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptDutyRosterOverrideStatus
            (HospitalDeptAppointmentStatusRequestDTO requestDTO, List<Long> rosterIdList);


    RosterDetailsForStatus fetchOverrideRosterDetails(RosterDetailsForStatus rosterDetailsForStatus,
                                                      Date apppointmentDate);

}
