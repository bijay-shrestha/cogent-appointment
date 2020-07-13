package com.cogent.cogentappointment.admin.repository.custom;


import com.cogent.cogentappointment.admin.dto.request.dashboard.DashBoardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.DoctorRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.dashboard.HospitalDepartmentRevenueRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AppointmentRevenueStatisticsResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DoctorRevenueDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.HospitalDepartmentRevenueDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.RevenueTrendResponseDTO;
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

    Double getRevenueByDates(Date toDate, Date fromDate, Long hospitalId, String appointmentServiceTypeCode);

    AppointmentRevenueStatisticsResponseDTO calculateAppointmentStatistics(String toDate,
                                                                           String fromDate,
                                                                           Long hospitalId,
                                                                           String appointmentServiceTypeCode);

    RevenueTrendResponseDTO getRevenueTrend(DashBoardRequestDTO dashBoardRequestDTO, Character filter);

    List<DoctorRevenueDTO> calculateDoctorRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                  Pageable pageable);

    List<DoctorRevenueDTO> calculateCancelledRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                     Pageable pageable);

    List<DoctorRevenueDTO> calculateRefundedRevenue(DoctorRevenueRequestDTO doctorRevenueRequestDTO,
                                                     Pageable pageable);

    List<HospitalDepartmentRevenueDTO> calculateHospitalDepartmentRevenue(HospitalDepartmentRevenueRequestDTO revenueRequestDTO,
                                                              Pageable pageable);

    List<HospitalDepartmentRevenueDTO> calculateCancelledHospitalDepartmentRevenue(HospitalDepartmentRevenueRequestDTO revenueRequestDTO,
                                                                          Pageable pageable);
}
