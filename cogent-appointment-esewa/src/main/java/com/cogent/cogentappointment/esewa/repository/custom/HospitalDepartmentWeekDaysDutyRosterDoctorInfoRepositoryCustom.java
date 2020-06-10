package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.appointmentHospitalDepartment.checkAvailability.HospitalDepartmentDoctorInfoResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 10/06/20
 */
@Repository
@Qualifier("HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom")
public interface HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {

    List<HospitalDepartmentDoctorInfoResponseDTO> fetchAvailableDoctors(Long hddRosterId, String weekDayCode);
}


