package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.SAVING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentLog.HOSPITAL_DEPARTMENT;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.log.constants.RoomLog.ROOM;
import static com.cogent.cogentappointment.client.utils.HospitalDepartmentUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.NameAndCodeValidationUtils.validateDuplicity;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa ON 5/20/20
 */

@Service
@Transactional
@Slf4j
public class HospitalDepartmentServiceImpl implements HospitalDepartmentService {

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final HospitalDepartmentChargeRepository hospitalDepartmentChargeRepository;

    private final HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository;

    private final HospitalDepartmentDoctorInfoRepository hospitalDepartmentDoctorInfoRepository;

    private final HospitalRepository hospitalRepository;

    private final DoctorRepository doctorRepository;

    private final RoomRepository roomRepository;

    public HospitalDepartmentServiceImpl(HospitalDepartmentRepository hospitalDepartmentRepository,
                                         HospitalDepartmentChargeRepository hospitalDepartmentChargeRepository,
                                         HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository,
                                         HospitalDepartmentDoctorInfoRepository hospitalDepartmentDoctorInfoRepository,
                                         HospitalRepository hospitalRepository,
                                         DoctorRepository doctorRepository,
                                         RoomRepository roomRepository) {
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.hospitalDepartmentChargeRepository = hospitalDepartmentChargeRepository;
        this.hospitalDepartmentRoomInfoRepository = hospitalDepartmentRoomInfoRepository;
        this.hospitalDepartmentDoctorInfoRepository = hospitalDepartmentDoctorInfoRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void save(HospitalDepartmentRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<Object[]> hospitalDepartments = hospitalDepartmentRepository.validateDuplicity(requestDTO, hospitalId);

        validateDuplicity(hospitalDepartments, requestDTO.getName(), requestDTO.getCode(),
                HospitalDepartment.class.getSimpleName());

        Hospital hospital = fetchHospitalById(hospitalId);

        HospitalDepartment  hospitalDepartment=saveHospitalDepartment(parseToHospitalDepartment(requestDTO,hospital));

        saveHospitalDepartmentCharge(parseToHospitalDepartmentCharge(requestDTO,hospitalDepartment));

        saveHospitalDepartmentDoctorInfo(requestDTO,hospitalDepartment);

        saveHospitalDepartmentRoomInfo(requestDTO,hospitalDepartment);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveHospitalDepartmentDoctorInfo(HospitalDepartmentRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment){

        List<Long> doctorIDList=requestDTO.getDoctorId();

        List<HospitalDepartmentDoctorInfo> hospitalDepartmentDoctorInfos=new ArrayList<>();

        doctorIDList.forEach(doctorID->{
            hospitalDepartmentDoctorInfos.add(parseToHospitalDepartmentDoctorInfo(hospitalDepartment,
                    requestDTO,
                    fetchActiveDoctor(doctorID,getLoggedInHospitalId())));
        });

        saveHospitalDepartmentDoctorInfo(hospitalDepartmentDoctorInfos);
    }

    private void saveHospitalDepartmentRoomInfo(HospitalDepartmentRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment){

        List<Long> roomIDList=requestDTO.getRoomId();

        List<HospitalDepartmentRoomInfo> hospitalDepartmentRoomInfos=new ArrayList<>();

        roomIDList.forEach(roomID->{
            hospitalDepartmentRoomInfos.add(parseToHospitalDepartmentRoomInfo(hospitalDepartment,
                    requestDTO,
                    fetchActiveRoom(roomID)));
        });

        saveHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfos);
    }

    private void saveHospitalDepartmentDoctorInfo(List<HospitalDepartmentDoctorInfo> doctorInfoList){
        hospitalDepartmentDoctorInfoRepository.saveAll(doctorInfoList);
    }

    private void saveHospitalDepartmentRoomInfo(List<HospitalDepartmentRoomInfo> roomInfoList){
        hospitalDepartmentRoomInfoRepository.saveAll(roomInfoList);
    }

    private HospitalDepartment saveHospitalDepartment(HospitalDepartment hospitalDepartment){
        return hospitalDepartmentRepository.save(hospitalDepartment);
    }

    private void saveHospitalDepartmentCharge(HospitalDepartmentCharge hospitalDepartmentCharge){
        hospitalDepartmentChargeRepository.save(hospitalDepartmentCharge);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Doctor fetchActiveDoctor(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchActiveDoctorByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Room fetchActiveRoom(Long id) {
        return roomRepository.fetchActiveRoomById(id)
                .orElseThrow(() -> ROOM_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> ROOM_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ROOM, id);
        throw new NoContentFoundException(Room.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };
}
