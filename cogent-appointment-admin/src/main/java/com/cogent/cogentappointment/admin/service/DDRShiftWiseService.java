package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.override.DDROverrideUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.update.shift.DDRShiftUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRMinResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDRDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.detail.DDROverrideBreakDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.weekDaysDetail.DDRWeekDaysResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.update.DDROverrideUpdateResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti on 08/05/20
 */
public interface DDRShiftWiseService {

    /*save*/
    void saveDDRWeekDaysDetail(DDRRequestDTO requestDTO);

    void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO);

    /*existing rosters*/
    DDRExistingMinResponseDTO fetchMinExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO);

    DDRExistingDetailResponseDTO fetchDetailExistingDDR(Long ddrId);

    List<DDRExistingWeekDaysResponseDTO> fetchExistingDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO);

    List<DDRBreakDetailResponseDTO> fetchWeekDaysBreakDetail(Long ddrWeekDaysDetailId);

    List<DDRBreakDetailResponseDTO> fetchExistingOverrideBreakDetail(Long ddrOverrideId);

    /*search*/
    List<DDRMinResponseDTO> search(DDRSearchRequestDTO searchRequestDTO, Pageable pageable);

    /*delete*/
    void delete(DeleteRequestDTO deleteRequestDTO);

    /*detail/edit modal*/
    DDRDetailResponseDTO fetchDetailsById(Long ddrId);

    List<DDRWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRWeekDaysRequestDTO requestDTO);

    List<DDROverrideBreakDetailResponseDTO> fetchDDROverrideBreakDetail(Long ddrOverrideId);

    /*update*/
    DDROverrideUpdateResponseDTO updateDoctorDutyRosterOverride(DDROverrideUpdateRequestDTO updateRequestDTO);

    void updateDDRShiftDetail(DDRShiftUpdateRequestDTO updateRequestDTO);
}
