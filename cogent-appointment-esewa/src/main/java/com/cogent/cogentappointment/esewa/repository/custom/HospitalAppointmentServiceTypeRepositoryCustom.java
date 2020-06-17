package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 27/05/20
 */
@Repository
@Qualifier("hospitalAppointmentServiceTypeRepositoryCustom")
public interface HospitalAppointmentServiceTypeRepositoryCustom {

    HospitalAppointmentServiceType fetchHospitalAppointmentServiceType(Long hospitalId,
                                                                       String appointmentServiceTypeCode);
}
