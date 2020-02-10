package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.weekdays.WeekDaysResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
@Repository
@Qualifier("weekDaysRepositoryCustom")
public interface WeekDaysRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveWeekDays();

    List<WeekDaysResponseDTO> fetchPrepareWeekDays();
}
