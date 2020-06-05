package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 27/05/20
 */
@Repository
@Qualifier("hospitalAppointmentServiceTypeRepositoryCustom")
public interface HospitalAppointmentServiceTypeRepositoryCustom {

    void updateIsPrimaryStatus(Long hospitalId, Long appointmentServiceTypeId);
}
