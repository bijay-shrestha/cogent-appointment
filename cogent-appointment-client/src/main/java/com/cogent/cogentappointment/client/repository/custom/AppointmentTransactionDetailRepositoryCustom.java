package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.client.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.client.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.DoctorRevenueDTO;
import com.cogent.cogentappointment.client.dto.response.dashboard.RevenueTrendResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 2019-10-22
 */
@Repository
@Qualifier("appointmentTransactionDetailRepositoryCustom")
public interface AppointmentTransactionDetailRepositoryCustom {

    Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId,String appointmentServiceTypeCode);

    AppointmentRevenueStatisticsResponseDTO calculateAppointmentStatistics(Date toDate, Date fromDate, Long hospitalId,
                                                                           String appointmentServiceTypeCode );

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO,
                                            Long hospitalId, Character filter);

    List<DoctorRevenueDTO> calculateDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,Pageable pagable);

    List<DoctorRevenueDTO> calculateCancelledRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,Pageable pagable);

}
