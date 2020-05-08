package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.DDRDetailRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.save.DDRRequestDTO;
import com.cogent.cogentappointment.admin.repository.DDRShiftWiseRepository;
import com.cogent.cogentappointment.admin.service.DDRShiftWiseService;
import com.cogent.cogentappointment.admin.service.DoctorService;
import com.cogent.cogentappointment.admin.service.HospitalService;
import com.cogent.cogentappointment.admin.service.SpecializationService;
import com.cogent.cogentappointment.persistence.model.Doctor;
import com.cogent.cogentappointment.persistence.model.DoctorDutyRosterShiftWise;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Specialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.DDRShiftWiseLog.DDR_SHIFT_WISE;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 08/05/20
 */
@Service
@Transactional
@Slf4j
public class DDRShiftWiseServiceImpl implements DDRShiftWiseService {

    private final DDRShiftWiseRepository ddrShiftWiseRepository;

    private final HospitalService hospitalService;

    private final SpecializationService specializationService;

    private final DoctorService doctorService;

    public DDRShiftWiseServiceImpl(DDRShiftWiseRepository ddrShiftWiseRepository,
                                   HospitalService hospitalService,
                                   SpecializationService specializationService,
                                   DoctorService doctorService) {
        this.ddrShiftWiseRepository = ddrShiftWiseRepository;
        this.hospitalService = hospitalService;
        this.specializationService = specializationService;
        this.doctorService = doctorService;
    }

    @Override
    public void save(DDRRequestDTO requestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, DDR_SHIFT_WISE);



        log.info(SAVING_PROCESS_COMPLETED, DDR_SHIFT_WISE, getDifferenceBetweenTwoTime(startTime));
    }

    private DoctorDutyRosterShiftWise saveDDRShiftWise(DDRDetailRequestDTO ddrDetail){

        Hospital hospital = fetchHospitalById(ddrDetail.getHospitalId());

        Specialization specialization = fetchSpecializationById(ddrDetail.getSpecializationId());

        Doctor doctor = fetchDoctorById(ddrDetail.getDoctorId());

//        DoctorDutyRosterShiftWise ddrShiftWise = pa

        return null;


    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalService.fetchActiveHospital(hospitalId);
    }

    private Doctor fetchDoctorById(Long doctorId) {
        return doctorService.fetchActiveDoctorById(doctorId);
    }

    private Specialization fetchSpecializationById(Long specializationId) {
        return specializationService.fetchActiveSpecializationById(specializationId);
    }



}
