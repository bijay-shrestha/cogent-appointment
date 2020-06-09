package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAppointmentStatusRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.HospitalDeptExistingDutyRosterRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptDutyRosterStatusResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.existing.HospitalDeptExistingDutyRosterResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRepositoryCustom")
public interface HospitalDeptDutyRosterRepositoryCustom {

    Character fetchRoomStatusIfExists(Long hospitalDepartmentId, Date fromDate, Date toDate);

    Character fetchRoomStatusIfExistsExceptCurrentId(Long hospitalDepartmentId, Date fromDate, Date toDate,
                                                     Long hddRosterId);

    List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable);

    HospitalDeptDutyRosterDetailResponseDTO fetchDetailsById(Long id);

    List<HospitalDeptExistingDutyRosterResponseDTO> fetchExistingDutyRosters(
            HospitalDeptExistingDutyRosterRequestDTO requestDTO);

    HospitalDeptExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long hddRosterId);

    List<HospitalDeptDutyRosterStatusResponseDTO> fetchHospitalDeptDutyRosterStatus(
            HospitalDeptAppointmentStatusRequestDTO requestDTO);
}
