package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 31/05/20
 */
@Repository
@Qualifier("hospitalDeptWeekDaysDutyRosterRepositoryCustom")
public interface HospitalDeptWeekDaysDutyRosterRepositoryCustom {

    HospitalDeptDutyRosterTimeResponseTO fetchWeekDaysTimeInfo(Long hddRosterId,  Date date);
}
