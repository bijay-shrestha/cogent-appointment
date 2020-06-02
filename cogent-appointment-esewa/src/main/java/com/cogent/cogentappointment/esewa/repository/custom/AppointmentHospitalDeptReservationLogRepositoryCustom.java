package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.request.appointmentHospitalDepartment.followup.AppointmentHospitalDeptFollowUpRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 02/06/20
 */
@Repository
@Qualifier("appointmentHospitalDeptReservationLogRepositoryCustom")
public interface AppointmentHospitalDeptReservationLogRepositoryCustom {

    Long fetchAppointmentHospitalDeptReservationLogId(AppointmentHospitalDeptFollowUpRequestDTO requestDTO);
}
