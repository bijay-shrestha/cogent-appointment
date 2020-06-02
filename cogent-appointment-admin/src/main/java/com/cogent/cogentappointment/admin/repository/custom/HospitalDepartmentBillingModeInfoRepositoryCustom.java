package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.billingMode.ChargeRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.ChargeResponseDTO;
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
