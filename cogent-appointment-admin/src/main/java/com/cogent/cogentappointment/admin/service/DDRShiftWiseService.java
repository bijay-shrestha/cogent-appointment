package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDROverrideBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDRDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRWeekDaysResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 08/05/20
 */
public interface DDRShiftWiseService {

    void saveDDRWeekDaysDetail(DDRRequestDTO requestDTO);

    void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO);

    DDRExistingMinResponseDTO fetchMinExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO);

    DDRExistingDetailResponseDTO fetchDetailExistingDDR(Long ddrId);

    List<DDRExistingWeekDaysResponseDTO> fetchExistingDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO);

    List<DDRBreakDetailResponseDTO> fetchWeekDaysBreakDetail(Long ddrWeekDaysDetailId);

    List<DDRBreakDetailResponseDTO> fetchExistingOverrideBreakDetail(Long ddrOverrideId);

    List<DDRMinResponseDTO> search(DDRSearchRequestDTO searchRequestDTO, Pageable pageable);

    void delete(DeleteRequestDTO deleteRequestDTO);

    DDRDetailResponseDTO fetchDetailsById(Long ddrId);

    List<DDRWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO);

    List<DDROverrideBreakDetailResponseDTO> fetchDDROverrideBreakDetail(Long ddrOverrideId);
}
