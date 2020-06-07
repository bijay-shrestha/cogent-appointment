package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeResponseDTO;
import com.cogent.cogentappointment.persistence.model.BillingMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/29/20
 */

@Repository
@Qualifier("billingModeRepositoryCustom")
public interface BillingModeRepositoryCustom {

    List<Object[]> validateDuplicity(BillingModeRequestDTO requestDTO);

    List<Object[]> validateDuplicity(BillingModeUpdateRequestDTO requestDTO);

    BillingModeMinimalResponseDTO search(BillingModeSearchRequestDTO searchRequestDTO,
                                         Pageable pageable);

    BillingModeResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinBillingMode();

    List<DropDownResponseDTO> fetchActiveMinBillingModeByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchMinBillingMode();

    List<DropDownResponseDTO> fetchMinBillingModeByHospitalId(Long hospitalId);

    BillingMode fetchBillingModeByHospitalId(Long hospitalId, Long billingModeId);

}
