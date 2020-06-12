package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.ChargeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentBillingModeInfoRepositoryCustom")
public interface HospitalDepartmentBillingModeInfoRepositoryCustom {

    ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO);
}
