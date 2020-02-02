package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.response.weekDays.WeekDaysMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 25/11/2019
 */
@Repository
@Qualifier("weekDaysRepositoryCustom")
public interface WeekDaysRepositoryCustom {

    List<WeekDaysMinResponseDTO> fetchActiveWeekDays();
}
