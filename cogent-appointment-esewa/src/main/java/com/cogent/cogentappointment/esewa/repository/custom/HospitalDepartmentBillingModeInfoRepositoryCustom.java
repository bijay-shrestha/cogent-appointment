package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentBillingModeInfoRepositoryCustom")
public interface HospitalDepartmentBillingModeInfoRepositoryCustom {

    List<DropDownResponseDTO> fetchBillingModeByDepartmentId(Long hospitalDepartmentId);

    Double fetchHospitalDeptAppointmentCharge(Long hospitalDepartmentBillingModeId, Long hospitalDepartmentId);

    Double fetchHospitalDeptAppointmentFollowUpCharge(Long hospitalDepartmentBillingModeId, Long hospitalDepartmentId);
}
