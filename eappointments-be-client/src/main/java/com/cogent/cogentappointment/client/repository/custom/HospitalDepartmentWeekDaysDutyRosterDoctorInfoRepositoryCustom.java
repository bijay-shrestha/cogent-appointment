package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAndWeekdaysDTO;
import com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus.HospitalDeptAndDoctorDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 07/06/20
 */
@Repository
@Qualifier("hospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom")
public interface HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {

    HospitalDeptAndDoctorDTO fetchHospitalDeptAndDoctorInfo(HospitalDeptAndWeekdaysDTO hospitalDeptAndWeekdaysDTO);
}
