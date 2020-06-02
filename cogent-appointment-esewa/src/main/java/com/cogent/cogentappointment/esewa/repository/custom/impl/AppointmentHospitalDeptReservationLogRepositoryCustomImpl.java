package com.cogent.cogentappointment.esewa.repository.custom.impl;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptReservationLogRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author smriti on 02/06/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentHospitalDeptReservationLogRepositoryCustomImpl
        implements AppointmentHospitalDeptReservationLogRepositoryCustom {
}
