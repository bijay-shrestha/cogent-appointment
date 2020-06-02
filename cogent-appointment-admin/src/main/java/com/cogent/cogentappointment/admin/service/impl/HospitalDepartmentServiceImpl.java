package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.*;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.ChargeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import com.cogent.cogentappointment.admin.exception.DataDuplicationException;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.*;
import com.cogent.cogentappointment.admin.service.HospitalDepartmentService;
import com.cogent.cogentappointment.persistence.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.ROOM_NUMBER_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.admin.log.constants.DoctorLog.DOCTOR;
import static com.cogent.cogentappointment.admin.log.constants.HospitalDepartmentLog.*;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.admin.log.constants.RoomLog.AVAILABLE_ROOM;
import static com.cogent.cogentappointment.admin.log.constants.RoomLog.ROOM;
import static com.cogent.cogentappointment.admin.utils.HospitalDepartmentUtils.*;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.admin.utils.commons.NameAndCodeValidationUtils.validateDuplicity;
import static java.lang.reflect.Array.get;


/**
 * @author Sauravi Thapa ON 5/20/20
 */

@Service
@Transactional
@Slf4j
public class HospitalDepartmentServiceImpl implements HospitalDepartmentService {

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    private final HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository;

    private final HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository;

    private final HospitalDepartmentDoctorInfoRepository hospitalDepartmentDoctorInfoRepository;

    private final HospitalRepository hospitalRepository;

    private final DoctorRepository doctorRepository;

    private final RoomRepository roomRepository;

    private final BillingModeRepository billingModeRepository;

    public HospitalDepartmentServiceImpl(HospitalDepartmentRepository hospitalDepartmentRepository,
                                         HospitalDepartmentBillingModeInfoRepository hospitalDepartmentBillingModeInfoRepository,
                                         HospitalDepartmentRoomInfoRepository hospitalDepartmentRoomInfoRepository,
                                         HospitalDepartmentDoctorInfoRepository hospitalDepartmentDoctorInfoRepository,
                                         HospitalRepository hospitalRepository,
                                         DoctorRepository doctorRepository,
                                         RoomRepository roomRepository,
                                         BillingModeRepository billingModeRepository) {
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
        this.hospitalDepartmentBillingModeInfoRepository = hospitalDepartmentBillingModeInfoRepository;
        this.hospitalDepartmentRoomInfoRepository = hospitalDepartmentRoomInfoRepository;
        this.hospitalDepartmentDoctorInfoRepository = hospitalDepartmentDoctorInfoRepository;
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.roomRepository = roomRepository;
        this.billingModeRepository = billingModeRepository;
    }

    @Override
    public void save(HospitalDepartmentRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        Long hospitalId = requestDTO.getHospitalId();

        List<Object[]> hospitalDepartments = hospitalDepartmentRepository.validateDuplicity(requestDTO, hospitalId);

        validateDuplicity(hospitalDepartments, requestDTO.getName(), requestDTO.getCode(),
                HospitalDepartment.class.getSimpleName());

        validateRoomNumber(requestDTO);

        Hospital hospital = fetchHospitalById(hospitalId);

        HospitalDepartment hospitalDepartment = saveHospitalDepartment(parseToHospitalDepartment(requestDTO, hospital));

        saveBillingModeWithCharge(requestDTO, hospitalDepartment);

        saveHospitalDepartmentDoctorInfo(requestDTO, hospitalDepartment);

        if (requestDTO.getRoomId().size() > 0)
            saveHospitalDepartmentRoomInfo(requestDTO, hospitalDepartment);

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(HospitalDepartmentUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartment hospitalDepartment = fetchHospitalDepartmentById(requestDTO.getId());

        saveHospitalDepartment(parseToUpdateHospitalDepartment(hospitalDepartment, requestDTO));

        updateHospitalDepartmentCharge(requestDTO, hospitalDepartment);

        if (requestDTO.getDoctorUpdateList().size() > 0)
            updateHospitalDepartmentDoctorInfo(requestDTO, hospitalDepartment);


        if (requestDTO.getRoomUpdateList().size() > 0)
            updateHospitalDepartmentRoomInfo(requestDTO, hospitalDepartment);


        log.info(UPDATING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

    }

    @Override
    public List<DropDownResponseDTO> fetchMinHospitalDepartment(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, HOSPITAL_DEPARTMENT);

        List<DropDownResponseDTO> minDepartment = hospitalDepartmentRepository.fetchMinHospitalDepartment(hospitalId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_NOT_FOUND());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinHospitalDepartment(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, HOSPITAL_DEPARTMENT);

        List<DropDownResponseDTO> minDepartment = hospitalDepartmentRepository.
                fetchActiveMinHospitalDepartment(hospitalId).orElseThrow(() -> HOSPITAL_DEPARTMENT_NOT_FOUND());

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, HOSPITAL_DEPARTMENT,
                getDifferenceBetweenTwoTime(startTime));

        return minDepartment;
    }

    @Override
    public List<DropDownResponseDTO> fetchAvailableRoom(Long hospitalId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_ACTIVE_DROPDOWN, AVAILABLE_ROOM);

        List<DropDownResponseDTO> minDepartment = hospitalDepartmentRepository.
                fetchAvailableHospitalDepartment(hospitalId).orElseThrow(() -> HOSPITAL_DEPARTMENT_NOT_FOUND());

        log.info(FETCHING_PROCESS_FOR_ACTIVE_DROPDOWN_COMPLETED, AVAILABLE_ROOM,
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

        HospitalDepartmentResponseDTO responseDTO =
                hospitalDepartmentRepository.fetchHospitalDepartmentDetails(id);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));

        return responseDTO;
    }

    @Override
    public void delete(DeleteRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartment hospitalDepartment = fetchHospitalDepartmentById(requestDTO.getId());


        saveHospitalDepartment(parseToDeleteHospitalDept(hospitalDepartment, requestDTO));

        deleteBillingModeWithCharge(requestDTO);

        deleteDoctorInfo(requestDTO);

        deleteRoomInfo(requestDTO);

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_DETAIL_PROCESS_STARTED, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO);

        ChargeResponseDTO chargeResponseDTO = hospitalDepartmentBillingModeInfoRepository
                .fetchAppointmentCharge(requestDTO);

        log.info(FETCHING_DETAIL_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO,
                getDifferenceBetweenTwoTime(startTime));

        return chargeResponseDTO;
    }

    public void saveBillingModeWithCharge(HospitalDepartmentRequestDTO hospitalRequestDTO,
                                          HospitalDepartment hospitalDepartment) {

        Long hospitalId = hospitalRequestDTO.getHospitalId();

        List<HospitalDepartmentBillingModeInfo> billingModeInfoList = new ArrayList<>();

        List<BillingModeChargeDTO> billingModeChargeDTOS = hospitalRequestDTO.getBillingModeChargeDTOList();

        billingModeChargeDTOS.forEach(billingModeChargeDTO -> {
            BillingMode billingMode = fetchBillingMode(billingModeChargeDTO.getBillingModeId(), hospitalId);
            billingModeInfoList.add(parseToHospitalDepartmentCharge(
                    billingModeChargeDTO, hospitalDepartment, billingMode));
        });
        saveHospitalDepartmentBillingModeInfoList(billingModeInfoList);
    }

    public void saveBillingModeWithCharge(BillingModeChargeUpdateDTO requestDTO,
                                          HospitalDepartment hospitalDepartment) {
        BillingMode billingMode = fetchBillingMode(requestDTO.getBillingModeId(),
                hospitalDepartment.getId());

        saveHospitalDepartmentBillingModeInfo(parseToUpdateHospitalDepartmentCharge(
                requestDTO, hospitalDepartment, billingMode));

    }

    public void validateRoomNumber(HospitalDepartmentRequestDTO requestDTO) {

        List<Long> roomIds = requestDTO.getRoomId();
        roomIds.forEach(roomId -> {
            List<Object[]> roomNumber = hospitalDepartmentRoomInfoRepository.validateRoomDuplicity(roomId,
                    requestDTO.getHospitalId());
            validateRoomDuplicity(roomNumber, roomId);
        });
    }

    public void validateRoomNumber(HospitalDepartmentUpdateRequestDTO requestDTO) {

        List<DepartmentRoomUpdateRequestDTO> roomIds = requestDTO.getRoomUpdateList();
        roomIds.forEach(roomId -> {
            List<Object[]> roomNumber = hospitalDepartmentRoomInfoRepository.validateRoomDuplicity(roomId.getRoomId(),
                    requestDTO.getHospitalId());
            validateRoomDuplicity(roomNumber, roomId.getRoomId());
        });
    }


    private void saveHospitalDepartmentDoctorInfo(HospitalDepartmentRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment) {
        Long hospitalId = requestDTO.getHospitalId();

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

        Long hospitalId = requestDTO.getHospitalId();

        List<Long> roomIDList = requestDTO.getRoomId();

        List<HospitalDepartmentRoomInfo> hospitalDepartmentRoomInfos = new ArrayList<>();

        roomIDList.forEach(roomID -> {
            hospitalDepartmentRoomInfos.add(parseToHospitalDepartmentRoomInfo(hospitalDepartment,
                    requestDTO.getStatus(),
                    fetchActiveRoom(roomID, hospitalId)));
        });

        saveHospitalDepartmentRoomInfo(hospitalDepartmentRoomInfos);
    }

    private void updateHospitalDepartmentCharge(HospitalDepartmentUpdateRequestDTO requestDTO,
                                                HospitalDepartment hospitalDepartment) {

        List<BillingModeChargeUpdateDTO> billingModeChargeUpdateDTOs = requestDTO.getBillingModeChargeUpdateDTOS();

        billingModeChargeUpdateDTOs.forEach(billingModeChargeUpdateDTO -> {
            if (!Objects.isNull(billingModeChargeUpdateDTO.getId())) {
                HospitalDepartmentBillingModeInfo hospitalDepartmentBillingModeInfo = fetchHospitalDepartmentCharge(
                        billingModeChargeUpdateDTO.getId());
                saveHospitalDepartmentBillingModeInfo(parseToUpdateHospitalDepartmentCharge(billingModeChargeUpdateDTO,
                        hospitalDepartmentBillingModeInfo,
                        hospitalDepartment));
            } else {
                saveBillingModeWithCharge(billingModeChargeUpdateDTO, hospitalDepartment);
            }
        });

    }

    private void updateHospitalDepartmentDoctorInfo(HospitalDepartmentUpdateRequestDTO requestDTO,
                                                    HospitalDepartment hospitalDepartment) {

        Long hospitaId = requestDTO.getHospitalId();

        List<DepartmentDoctorUpdateRequestDTO> doctorUpdateRequestDTOS = requestDTO.getDoctorUpdateList();

        doctorUpdateRequestDTOS.forEach(doctorUpdateRequestDTO -> {
            if (doctorUpdateRequestDTO.getStatus().equals(YES)) {
                saveHospitalDepartmentDoctorInfo(parseToHospitalDepartmentDoctorInfo(
                        hospitalDepartment,
                        doctorUpdateRequestDTO.getStatus(),
                        fetchActiveDoctor(doctorUpdateRequestDTO.getDoctorId(), hospitaId)));
            } else {
                deleteDoctor(doctorUpdateRequestDTO, requestDTO.getId(), requestDTO.getRemarks());
            }
        });
    }

    private void updateHospitalDepartmentRoomInfo(HospitalDepartmentUpdateRequestDTO requestDTO,
                                                  HospitalDepartment hospitalDepartment) {


        Long hospitaId = requestDTO.getHospitalId();

        List<DepartmentRoomUpdateRequestDTO> roomUpdateRequestDTOS = requestDTO.getRoomUpdateList();

        roomUpdateRequestDTOS.forEach(roomUpdateRequestDTO -> {
            if (roomUpdateRequestDTO.getStatus().equals(YES)) {
                validateRoomNumber(requestDTO);
                saveHospitalDepartmentRoomInfo(parseToHospitalDepartmentRoomInfo(
                        hospitalDepartment,
                        roomUpdateRequestDTO.getStatus(),
                        fetchActiveRoom(roomUpdateRequestDTO.getRoomId(), hospitaId)));
            } else {
                deleteRoom(roomUpdateRequestDTO, requestDTO.getId(), requestDTO.getRemarks());
            }
        });

    }

    public void deleteDoctor(DepartmentDoctorUpdateRequestDTO requestDTO, Long hospitalDepartmentId, String remarks) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartmentDoctorInfo hospitalDeptDoctorInfo = hospitalDepartmentDoctorInfoRepository
                .fetchDoctorByHospitalDepartmentId(hospitalDepartmentId, requestDTO.getDoctorId());

        saveHospitalDepartmentDoctorInfo(parseToDeleteHospitalDeptDoctorInfo(hospitalDeptDoctorInfo,
                requestDTO.getStatus(),
                remarks));

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    public void deleteRoom(DepartmentRoomUpdateRequestDTO requestDTO, Long hospitalDepartmentId, String remarks) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, HOSPITAL_DEPARTMENT);

        HospitalDepartmentRoomInfo hospitalDeptRoomInfo = hospitalDepartmentRoomInfoRepository
                .fetchRoomByHospitalDepartmentId(hospitalDepartmentId, requestDTO.getRoomId());

        saveHospitalDepartmentRoomInfo(parseToDeleteHospitalDeptRoomInfo(hospitalDeptRoomInfo,
                requestDTO.getStatus(),
                remarks));

        log.info(DELETING_PROCESS_COMPLETED, HOSPITAL_DEPARTMENT, getDifferenceBetweenTwoTime(startTime));
    }

    public void deleteRoomInfo(DeleteRequestDTO requestDTO) {

        List<HospitalDepartmentRoomInfo> roomInfos = fetchExisitngRoomList(requestDTO.getId());
        saveHospitalDepartmentRoomInfo(parseToDeleteHospitalDeptRoomInfos(roomInfos, requestDTO));
    }

    public void deleteDoctorInfo(DeleteRequestDTO requestDTO) {

        List<HospitalDepartmentDoctorInfo> doctorInfos = fetchExisitngDoctorList(requestDTO.getId());
        saveHospitalDepartmentDoctorInfo(parseToDeleteHospitalDeptDoctorInfos(doctorInfos, requestDTO));
    }

    public void deleteBillingModeWithCharge(DeleteRequestDTO requestDTO) {

        List<HospitalDepartmentBillingModeInfo> hospitalDepartmentBillingModeInfoList =
                fetchHospitalDepartmentChargeListByHospitalDepartmentId(requestDTO.getId());

        saveHospitalDepartmentBillingModeInfoList(parseToDeleteHospitalDeptCharge(hospitalDepartmentBillingModeInfoList,
                requestDTO));

    }

    public static void validateRoomDuplicity(List<Object[]> objects,
                                             Long requestedRoomId) {
        final int ID = 0;
        final int ROOM_NUMBER = 1;

        objects.forEach(object -> {
            if (requestedRoomId == (get(object, ID)))
                throw new DataDuplicationException(
                        String.format(ROOM_NUMBER_DUPLICATION_MESSAGE, (get(object, ROOM_NUMBER))));
        });
    }

    private BillingMode fetchBillingMode(Long id, Long hospitalId) {
        return billingModeRepository.fetchBillingModeByHospitalId(hospitalId, id);
    }

    private List<HospitalDepartmentRoomInfo> fetchExisitngRoomList(Long hospitalDepartmentId) {
        return hospitalDepartmentRoomInfoRepository.fetchRoomListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private List<HospitalDepartmentDoctorInfo> fetchExisitngDoctorList(Long hospitalDepartmentId) {
        return hospitalDepartmentDoctorInfoRepository.fetchDoctorListByHospitalDepartmentId(hospitalDepartmentId);
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

    private void saveHospitalDepartmentBillingModeInfoList(List<HospitalDepartmentBillingModeInfo>
                                                                   hospitalDepartmentBillingModeInfo) {
        hospitalDepartmentBillingModeInfoRepository.saveAll(hospitalDepartmentBillingModeInfo);
    }

    private void saveHospitalDepartmentBillingModeInfo(HospitalDepartmentBillingModeInfo
                                                               hospitalDepartmentBillingModeInfo) {
        hospitalDepartmentBillingModeInfoRepository.save(hospitalDepartmentBillingModeInfo);
    }

    private HospitalDepartment fetchHospitalDepartmentById(Long hospitalDepartmentId) {
        return hospitalDepartmentRepository.fetchById(hospitalDepartmentId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private HospitalDepartmentBillingModeInfo fetchHospitalDepartmentChargeByHospitalDepartmentId(Long hospitalDepartmentId) {
        return hospitalDepartmentBillingModeInfoRepository.fetchByHospitalDepartmentId(hospitalDepartmentId)
                .orElseThrow(() -> HOSPITAL_DEPARTMENT_CHARGE_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalDepartmentId));
    }

    private List<HospitalDepartmentBillingModeInfo> fetchHospitalDepartmentChargeListByHospitalDepartmentId(Long hospitalDepartmentId) {
        return hospitalDepartmentBillingModeInfoRepository.fetchListByHospitalDepartmentId(hospitalDepartmentId);
    }

    private HospitalDepartmentBillingModeInfo fetchHospitalDepartmentCharge(Long id) {
        return hospitalDepartmentBillingModeInfoRepository.fetchById(id);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Doctor fetchActiveDoctor(Long doctorId, Long hospitalId) {
        return doctorRepository.fetchDoctorById(doctorId, hospitalId)
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
                log.error(CONTENT_NOT_FOUND_BY_HOSPITAL_DEPARTMENT_ID, HOSPITAL_DEPARTMENT_BILLING_MODE_INFO, hospitalDepartmentId);
                throw new NoContentFoundException(HospitalDepartmentBillingModeInfo.class, "hospitalDepartmentId",
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
