package com.cogent.cogentappointment.esewa.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 10/06/20
 */
@Repository
@Qualifier("HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom")
public interface HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {

    List<String> fetchAvailableDoctors(Long hddRosterId, String weekDayCode);
}


