package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingWeekDaysRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingWeekDaysResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 13/05/20
 */
@Repository
@Qualifier("ddrWeekDaysDetailRepositoryCustom")
public interface DDRWeekDaysDetailRepositoryCustom {

    List<DDRExistingWeekDaysResponseDTO> fetchDDRWeekDaysDetail(DDRExistingWeekDaysRequestDTO requestDTO);
}
