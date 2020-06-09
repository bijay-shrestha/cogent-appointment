package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.billingMode.BillingModeResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public interface BillingModeService {
    void save(BillingModeRequestDTO requestDTO);

    void update(BillingModeUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    BillingModeMinimalResponseDTO search(BillingModeSearchRequestDTO searchRequestDTO,
                                         Pageable pageable);

    BillingModeResponseDTO fetchDetailsById(Long id);

    List<DropDownResponseDTO> fetchActiveMinBillingMode();

    List<DropDownResponseDTO> fetchActiveMinBillingModeByHospitalId(Long hospitalId);

    List<DropDownResponseDTO> fetchMinBillingMode();

    List<DropDownResponseDTO> fetchMinBillingModeByHospitalId(Long hospitalId);

}
