package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.specializationDutyRoster.HospitalDeptDutyRosterMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("specializationDutyRosterRepositoryCustom")
public interface HospitalDeptDutyRosterRepositoryCustom {

    Long validateSpecializationDutyRosterCount(Long specializationId,
                                       Date fromDate,
                                       Date toDate);

    List<HospitalDeptDutyRosterMinResponseDTO> search(HospitalDeptDutyRosterSearchRequestDTO searchRequestDTO,
                                                      Pageable pageable, Long hospitalId);
//
//    DoctorDutyRosterDetailResponseDTO fetchDetailsById(Long id, Long hospitalId);
//
//    DoctorDutyRosterTimeResponseDTO fetchDoctorDutyRosterTime(Date date,
//                                                              Long specializationId);
//
//    List<DoctorExistingDutyRosterResponseDTO> fetchExistingDoctorDutyRosters(
//            DoctorExistingDutyRosterRequestDTO requestDTO, Long hospitalId);
//
//    DoctorExistingDutyRosterDetailResponseDTO fetchExistingRosterDetails(Long specializationDutyRosterId);
//
//    List<DoctorDutyRosterStatusResponseDTO> fetchDoctorDutyRosterStatus
//            (AppointmentStatusRequestDTO requestDTO,
//             Long hospitalId);
//
//    List<DoctorDutyRosterAppointmentDate> getDutyRosterByDoctorAndSpecializationId(AppointmentDatesRequestDTO requestDTO);
//
//    List<DoctorWeekDaysDutyRosterAppointmentDate> getWeekDaysDutyRosterDataByDutyRosterId(Long specializationDutyRosterId);
//
//    List<String> getWeekDaysDutyRosterByDutyRosterId(Long specializationDutyRosterId);

}
