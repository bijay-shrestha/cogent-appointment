package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.override.DDROverrideRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.weekDaysDetail.DDRRequestDTO;

/**
 * @author smriti on 08/05/20
 */
public interface DDRShiftWiseService {

    void saveDDRWeekDaysDetail(DDRRequestDTO requestDTO);

    void saveDDROverrideDetail(DDROverrideRequestDTO requestDTO);
}
