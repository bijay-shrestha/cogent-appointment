package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 08/05/20
 */
@Repository
@Qualifier("ddrShiftWiseRepositoryCustom")
public interface DDRShiftWiseRepositoryCustom {

    Long validateDoctorDutyRosterCount(Long doctorId,
                                       Long specializationId,
                                       Date fromDate,
                                       Date toDate);

}
