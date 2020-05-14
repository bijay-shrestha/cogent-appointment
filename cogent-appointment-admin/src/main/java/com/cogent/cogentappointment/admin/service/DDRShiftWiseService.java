package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingWeekDaysResponseDTO;

import java.util.List;

/**
 * @author smriti on 08/05/20
 */
public interface DDRShiftWiseService {

    void saveDDRWeekDaysDetail(DDRRequestDTO requestDTO);

    void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO);

    DDRExistingMinResponseDTO fetchMinExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO);

    DDRExistingDetailResponseDTO fetchDetailExistingDDR(Long ddrId);

    List<DDRExistingWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRExistingWeekDaysRequestDTO requestDTO);

    List<DDRBreakDetailResponseDTO> fetchWeekDaysBreakDetail(Long ddrWeekDaysDetailId);

    List<DDRBreakDetailResponseDTO> fetchOverrideBreakDetail(Long ddrOverrideDetailId);

}
