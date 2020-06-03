package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.*;
import com.cogent.cogentappointment.admin.dto.response.billingMode.*;
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

    List<DropDownResponseDTO> fetchMinBillingMode();

}
