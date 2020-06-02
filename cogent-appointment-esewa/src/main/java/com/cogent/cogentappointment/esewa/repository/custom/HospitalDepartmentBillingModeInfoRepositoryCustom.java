package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.ChargeResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentBillingModeInfoRepositoryCustom")
public interface HospitalDepartmentBillingModeInfoRepositoryCustom {

    ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO);

    List<DropDownResponseDTO> fetchBillingModeByDepartmentId(Long hospitalDepartmentId);
}
