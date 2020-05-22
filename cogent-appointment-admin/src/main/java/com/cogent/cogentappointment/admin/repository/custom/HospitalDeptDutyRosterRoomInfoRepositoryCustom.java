package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRoomRepositoryCustom")
public interface HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    Long fetchRoomCount(Long hospitalDeptId, Date fromDate, Date toDate, Long roomId);
}


