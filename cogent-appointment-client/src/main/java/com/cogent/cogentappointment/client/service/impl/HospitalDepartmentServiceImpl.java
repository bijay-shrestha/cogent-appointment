package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentDeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.*;
import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.client.log.constants.HospitalDepartmentLog.*;
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

        HospitalDepartment hospitalDepartment = saveHospitalDepartment(parseToHospitalDepartment(requestDTO, hospital));

        saveHospitalDepartmentCharge(parseToHospitalDepartmentCharge(requestDTO, hospitalDepartment));

        saveHospitalDepartmentDoctorInfo(requestDTO, hospitalDepartment);

        saveHospitalDepartmentRoomInfo(requestDTO, hospitalDepartment);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(HospitalDepartmentUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        HospitalDepartment hospitalDepartment = fetchHospitalDepartmentById(requestDTO.getId(), hospitalId);

        saveHospitalDepartment(parseToUpdateHospitalDepartment(hospitalDepartment, requestDTO));

        updateHospitalDepartmentCharge(requestDTO);

        updateHospitalDepartmentDoctorInfo(requestDTO, hospitalDepartment);

        updateHospitalDepartmentRoomInfo(requestDTO, hospitalDepartment);

        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public List<DropDownResponseDTO> fetchMinHospitalDepartment() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<DropDownResponseDTO> minDepartment = hospitalDepartmentRepository.fetchMinHospitalDepartment(hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_NOT_FOUND());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinHospitalDepartment() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        List<DropDownResponseDTO> minDepartment = hospitalDepartmentRepository.
                fetchActiveMinHospitalDepartment(hospitalId).orElseThrow(() -> HOSPITAL_DEPARTMENT_NOT_FOUND());

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, HOSPITAL_DEPARTMENT,
                getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO searchRequestDTO,
                                                             Pageable pageable) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartmentMinimalResponseDTO responseDTO =
                hospitalDepartmentRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public HospitalDepartmentResponseDTO fetchHospitalDepartmentDetails(Long id) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        HospitalDepartmentResponseDTO responseDTO =
                hospitalDepartmentRepository.fetchHospitalDepartmentDetails(id, hospitalId);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void delete(DeleteRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        Long hospitalId = getLoggedInHospitalId();

        HospitalDepartment hospitalDepartment = fetchHospitalDepartmentById(requestDTO.getId(), hospitalId);

        HospitalDepartmentCharge hospitalDepartmentCharge = fetchHospitalDepartmentChargeByHospitalDepartmentId(requestDTO.getId());

        saveHospitalDepartment(parseToDeleteHospitalDept(hospitalDepartment,requestDTO));

        saveHospitalDepartmentCharge(parseToDeleteHospitalDeptCharge(hospitalDepartmentCharge,requestDTO));

        deleteDoctorInfo(requestDTO);

        deleteRoomInfo(requestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void deleteDoctor(HospitalDepartmentDeleteRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartmentDoctorInfo hospitalDeptDoctorInfo=hospitalDepartmentDoctorInfoRepository
                .fetchDoctorByHospitalDepartmentId(requestDTO.getHospitalDepartmentId(),requestDTO.getId());

        hospitalDeptDoctorInfo.setStatus(requestDTO.getStatus());

        hospitalDeptDoctorInfo.setRemarks(requestDTO.getRemarks());

        saveHospitalDepartmentDoctorInfo(hospitalDeptDoctorInfo);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void deleteRoom(HospitalDepartmentDeleteRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartmentRoomInfo hospitalDeptRoomInfo=hospitalDepartmentRoomInfoRepository
                .fetchRoomByHospitalDepartmentId(requestDTO.getHospitalDepartmentId(),requestDTO.getId());

        hospitalDeptRoomInfo.setStatus(requestDTO.getStatus());

        hospitalDeptRoomInfo.setRemarks(requestDTO.getRemarks());

        saveHospitalDepartmentRoomInfo(hospitalDeptRoomInfo);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveHospitalDepartmentDoctorInfo(HospitalDepartmentRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment) {
        Long hospitalId = getLoggedInHospitalId();

        List<Long> doctorIDList = requestDTO.getDoctorId();

        List<HospitalDepartmentDoctorInfo> hospitalDepartmentDoctorInfos = new ArrayList<>();

        doctorIDList.forEach(doctorID -> {
            hospitalDepartmentDoctorInfos.add(parseToHospitalDepartmentDoctorInfo(hospitalDepartment,
                    requestDTO.getStatus(),
                    fetchActiveDoctor(doctorID, hospitalId)));
        });

        saveHospitalDepartmentDoctorInfo(hospitalDepartmentDoctorInfos);
    }

    private void saveHospitalDepartmentRoomInfo(HospitalDepartmentRequestDTO requestDTO,
                                                HospitalDepartment hospitalDepartment) {

        Long hospitalId = getLoggedInHospitalId();

        List<Long> roomIDList = requestDTO.getRoomId();

        List<HospitalDepartmentRoomInfo> hospitalDepartmentRoomInfos = new ArrayList<>();

        roomIDList.forEach(roomID -> {
            hospitalDepartmentRoomInfos.add(parseToHospitalDepartmentRoomInfo(hospitalDepartment,
                    requestDTO.getStatus(),
                    fetchActiveRoom(roomID, hospitalId)));
        });

        saveHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfos);
    }

    private void updateHospitalDepartmentCharge(HospitalDepartmentUpdateRequestDTO requestDTO) {
        HospitalDepartmentCharge hospitalDepartmentCharge = fetchHospitalDepartmentChargeByHospitalDepartmentId
                (requestDTO.getId());

        if (hospitalDepartmentCharge.getAppointmentCharge() != requestDTO.getAppointmentCharge() ||
                hospitalDepartmentCharge.getAppointmentFollowUpCharge() != requestDTO.getFollowUpCharge()) {
            saveHospitalDepartmentCharge(parseToUpdateHospitalDepartmentCharge(hospitalDepartmentCharge, requestDTO));
        }

    }

    private void updateHospitalDepartmentDoctorInfo(HospitalDepartmentUpdateRequestDTO requestDTO,
                                                    HospitalDepartment hospitalDepartment) {

        Long hospitalId = getLoggedInHospitalId();

        List<Long> exisitngActiveDoctorIdList = fetchExisitngActiveDoctorIdList(requestDTO.getId());

        List<Long> newDoctorIdList = mergeExisitingAndNewListId(exisitngActiveDoctorIdList, requestDTO.getDoctorId());

        if (newDoctorIdList.size() > 0) {
            List<HospitalDepartmentDoctorInfo> hospitalDepartmentDoctorInfos = new ArrayList<>();

            newDoctorIdList.forEach(doctorID -> {
                hospitalDepartmentDoctorInfos.add(parseToHospitalDepartmentDoctorInfo(hospitalDepartment,
                        requestDTO.getStatus(),
                        fetchActiveDoctor(doctorID, hospitalId)));
            });

            saveHospitalDepartmentDoctorInfo(hospitalDepartmentDoctorInfos);
        }

    }

    private void updateHospitalDepartmentRoomInfo(HospitalDepartmentUpdateRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment) {

        Long hospitalId = getLoggedInHospitalId();

        List<Long> exisitngActiveRoomIdList = fetchExisitngActiveRoomIdList(requestDTO.getId());

        List<Long> newRoomIdList = mergeExisitingAndNewListId(exisitngActiveRoomIdList, requestDTO.getRoomId());

        if (newRoomIdList.size() > 0) {
            List<HospitalDepartmentRoomInfo> hospitalDepartmentRoomInfos = new ArrayList<>();

            newRoomIdList.forEach(roomID -> {
                hospitalDepartmentRoomInfos.add(parseToHospitalDepartmentRoomInfo(hospitalDepartment,
                        requestDTO.getStatus(),
                        fetchActiveRoom(roomID, hospitalId)));
            });

            saveHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfos);
        }

    }

    public void deleteRoomInfo(DeleteRequestDTO requestDTO){

        List<HospitalDepartmentRoomInfo> roomInfos=fetchExisitngRoomList(requestDTO.getId());
        saveHospitalDepartmentRoomInfo(parseToDeleteHospitalDeptRoomInfo(roomInfos,requestDTO));
    }

    public void deleteDoctorInfo(DeleteRequestDTO requestDTO){

        List<HospitalDepartmentDoctorInfo> doctorInfos=fetchExisitngDoctorList(requestDTO.getId());
        saveHospitalDepartmentDoctorInfo(parseToDeleteHospitalDeptDoctorInfo(doctorInfos,requestDTO));
    }

    private List<HospitalDepartmentRoomInfo> fetchExisitngRoomList(Long hospitalDepartmentId) {
        return hospitalDepartmentRoomInfoRepository.fetchRoomListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private List<HospitalDepartmentDoctorInfo> fetchExisitngDoctorList(Long hospitalDepartmentId) {
        return hospitalDepartmentDoctorInfoRepository.fetchDoctorListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private List<Long> fetchExisitngActiveDoctorIdList(Long hospitalDepartmentId) {
        return hospitalDepartmentDoctorInfoRepository.fetchDoctorIdListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private List<Long> fetchExisitngActiveRoomIdList(Long hospitalDepartmentId) {
        return hospitalDepartmentRoomInfoRepository.fetchRoomIdListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private void saveHospitalDepartmentDoctorInfo(List<HospitalDepartmentDoctorInfo> doctorInfoList) {
        hospitalDepartmentDoctorInfoRepository.saveAll(doctorInfoList);
    }

    private void saveHospitalDepartmentDoctorInfo(HospitalDepartmentDoctorInfo doctorInfo) {
        hospitalDepartmentDoctorInfoRepository.save(doctorInfo);
    }

    private void saveHospitalDepartmentRoomInfo(List<HospitalDepartmentRoomInfo> roomInfoList) {
        hospitalDepartmentRoomInfoRepository.saveAll(roomInfoList);
    }

    private void saveHospitalDepartmentRoomInfo(HospitalDepartmentRoomInfo roomInfo) {
        hospitalDepartmentRoomInfoRepository.save(roomInfo);
    }

    private HospitalDepartment saveHospitalDepartment(HospitalDepartment hospitalDepartment) {
        return hospitalDepartmentRepository.save(hospitalDepartment);
    }

    private void saveHospitalDepartmentCharge(HospitalDepartmentCharge hospitalDepartmentCharge) {
        hospitalDepartmentChargeRepository.save(hospitalDepartmentCharge);
    }

    private HospitalDepartment fetchHospitalDepartmentById(Long hospitalDepartmentId, Long hospitalId) {
        return hospitalDepartmentRepository.fetchByIdAndHospitalId(hospitalDepartmentId, hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private HospitalDepartmentCharge fetchHospitalDepartmentChargeByHospitalDepartmentId(Long hospitalDepartmentId) {
        return hospitalDepartmentChargeRepository.fetchByHospitalDepartmentId(hospitalDepartmentId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_CHARGE_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Doctor fetchActiveDoctor(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchActiveDoctorByIdAndHospitalId(doctorId, hospitalId)
                .orElseThrow(() -> DOCTOR_WITH_GIVEN_ID_NOT_FOUND.apply(doctorId));
    }

    private Room fetchActiveRoom(Long id, Long hospitalId) {
        return roomRepository.fetchActiveRoomByIdAndHospitalId(id, hospitalId)
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

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL_DEPARTMENT, id);
        throw new NoContentFoundException(HospitalDepartment.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> HOSPITAL_DEPARTMENT_CHARGE_WITH_GIVEN_ID_NOT_FOUND =
            (hospitalDepartmentId) -> {
                log.error(CONTENT_NOT_FOUND_BY_HOSPITAL_DEPARTMENT_ID, HOSPITAL_DEPARTMENT_CHARGE, hospitalDepartmentId);
                throw new NoContentFoundException(HospitalDepartmentCharge.class, "hospitalDepartmentId",
                        hospitalDepartmentId.toString());
            };

    private Function<Long, NoContentFoundException> DOCTOR_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, DOCTOR, id);
        throw new NoContentFoundException(Doctor.class, "id", id.toString());
    };

    private NoContentFoundException HOSPITAL_DEPARTMENT_NOT_FOUND() {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_DEPARTMENT);
        throw new NoContentFoundException(HospitalDepartment.class);
    }
}
