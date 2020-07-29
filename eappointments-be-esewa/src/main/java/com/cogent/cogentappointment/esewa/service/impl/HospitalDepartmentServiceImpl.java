package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartment.HospitalDepartmentWithStatusResponseDTO;
import com.cogent.cogentappointment.esewa.repository.HospitalDepartmentBillingModeInfoRepository;
import com.cogent.cogentappointment.esewa.repository.HospitalDepartmentRepository;
import com.cogent.cogentappointment.esewa.service.HospitalDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.esewa.log.constants.HospitalDepartmentLog.*;
import static com.cogent.cogentappointment.esewa.utils.HospitalDepartmentUtils.parseHospitalDepartmentDetails;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 28/05/20
 */
@Service
@Transactional
@Slf4j
public class HospitalDepartmentServiceImpl implements HospitalDepartmentService {

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository;

    public HospitalDepartmentServiceImpl(HospitalDepartmentRepository hospitalDepartmentRepository,
                                         HospitalDepartmentBillingModeInfoRepository
                                                 hospitalDepartmentBillingModeInfoRepository) {
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.hospitalDepartmentBillingModeInfoRepository = hospitalDepartmentBillingModeInfoRepository;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinHospitalDepartment(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        List<DropDownResponseDTO> minDepartment =
                hospitalDepartmentRepository.fetchActiveHospitalDepartment(hospitalId);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT,
                getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public List<DropDownResponseDTO> fetchBillingModeByDepartmentId(Long hospitalDepartmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_BILLING_MODE_DROP_DOWN_PROCESS_STARTED);

        List<DropDownResponseDTO> response = hospitalDepartmentBillingModeInfoRepository.
                fetchBillingModeByDepartmentId(hospitalDepartmentId);

        log.info(FETCHING_BILLING_MODE_DROP_DOWN_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return response;
    }

    @Override
    public HospitalDepartmentWithStatusResponseDTO fetchHospitalDepartmentInfo(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT_AND_BILLING_MODE_INFO);

        List<HospitalDepartmentResponseDTO> availableHospitalDepartments =
                hospitalDepartmentRepository.fetchHospitalDepartmentInfo(hospitalId);

        HospitalDepartmentWithStatusResponseDTO responseDTO =
                parseHospitalDepartmentDetails(availableHospitalDepartments);

        log.info(FETCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_AND_BILLING_MODE_INFO,
                getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }
}
