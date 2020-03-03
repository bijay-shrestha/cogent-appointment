package com.cogent.cogentappointment.admin.repository.custom;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentTransactionDetailRepositoryCustom")
public interface AppointmentTransactionDetailRepositoryCustom {

    Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId);

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO, Character filter);
}
