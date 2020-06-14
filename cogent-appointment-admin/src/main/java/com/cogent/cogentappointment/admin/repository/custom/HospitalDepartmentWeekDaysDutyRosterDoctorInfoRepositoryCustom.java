package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus.hospitalDepartmentStatus.HospitalDeptAndWeekdaysDTO;
import com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus.HospitalDeptAndDoctorDTO;
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
