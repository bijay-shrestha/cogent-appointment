package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRepositoryCustom")
public interface HospitalDeptDutyRosterRepositoryCustom {

    List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Date date,
                                                                   Long hospitalDepartmentId);
}
