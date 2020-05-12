package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingAvailabilityResponseDTO;

/**
 * @author smriti on 08/05/20
 */
public interface DDRShiftWiseService {

    void saveDDRWeekDaysDetail(DDRRequestDTO requestDTO);

    void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO);

    DDRExistingAvailabilityResponseDTO fetchExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO);
}
