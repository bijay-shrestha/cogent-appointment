package com.cogent.cogentappointment.client.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author Sauravi Thapa ON 5/18/209
 */
@Repository
@Qualifier("specializationDutyRosterOverrideRepositoryCustom")
public interface SpecializationDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCount( Long specializationId, Date fromDate, Date toDate);
}
