package com.cogent.cogentappointment.client.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterOverrideRepositoryCustom")
public interface HospitalDeptDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCount( Long hospitalDepartmentId, Date fromDate, Date toDate);
}
