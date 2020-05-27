package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.room.RoomMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.DataDuplicationException;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.HospitalRepository;
import com.cogent.cogentappointment.client.repository.RoomRepository;
import com.cogent.cogentappointment.client.service.RoomService;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.ROOM_NUMBER_DUPLICATION_MESSAGE;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.client.log.constants.HospitalLog.HOSPITAL;
import static com.cogent.cogentappointment.client.log.constants.RoomLog.ROOM;
import static com.cogent.cogentappointment.client.log.constants.RoomLog.ROOM_NUMBER_DUPLICATION_ERROR;
import static com.cogent.cogentappointment.client.utils.RoomUtils.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

/**
 * @author Sauravi Thapa ON 5/19/20
 */

@Service
@Transactional
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final HospitalRepository hospitalRepository;

    public RoomServiceImpl(RoomRepository roomRepository,
                           HospitalRepository hospitalRepository) {
        this.roomRepository = roomRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void save(RoomRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, ROOM);

        Long hospitalId = getLoggedInHospitalId();

        Long count = roomRepository.validateDuplicity(requestDTO.getRoomNumber(), hospitalId);

        validateRoomNumber(requestDTO.getRoomNumber(), count);

        Hospital hospital = fetchHospitalById(hospitalId);

        save(parseToRoom(requestDTO, hospital));

        log.info(SAVING_PROCESS_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void update(RoomUpdateRequestDTO requestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(UPDATING_PROCESS_STARTED, ROOM);

        Long hospitalId = getLoggedInHospitalId();

        Room room = fetchRoomById(requestDTO.getId());

        Long count = roomRepository.validateDuplicity(
                requestDTO.getId(),
                requestDTO.getRoomNumber(),
                hospitalId);

        validateRoomNumber(requestDTO.getRoomNumber(), count);

        Hospital hospital = fetchHospitalById(hospitalId);

        save(parseToUpdatedRoom(requestDTO, room, hospital));

        log.info(UPDATING_PROCESS_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(DELETING_PROCESS_STARTED, ROOM);

        Room room = fetchRoomById(deleteRequestDTO.getId());

        save(parseToDeletedRoom(room, deleteRequestDTO));

        log.info(DELETING_PROCESS_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinRoom() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ROOM);

        List<DropDownResponseDTO> responseDTOS = roomRepository.fetchActiveMinRoom(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinRoom() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ROOM);

        List<DropDownResponseDTO> responseDTOS = roomRepository.fetchMinRoom(getLoggedInHospitalId());

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public RoomMinimalResponseDTO search(RoomSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SEARCHING_PROCESS_STARTED, ROOM);

        RoomMinimalResponseDTO responseDTOS = roomRepository.search(searchRequestDTO, pageable);

        log.info(SEARCHING_PROCESS_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public Room fetchActiveRoom(Long roomId) {
        return roomRepository.fetchActiveRoomById(roomId)
                .orElseThrow(()-> ROOM_WITH_GIVEN_ID_NOT_FOUND.apply(roomId));
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinRoomByHospitalDepartmentId(Long hospitalDepartmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ROOM);

        List<DropDownResponseDTO> responseDTOS = roomRepository.fetchActiveMinRoomByHospitalDepartmentId(hospitalDepartmentId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinRoomByHospitalDepartmentId(Long hospitalDepartmentId) {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED_FOR_DROPDOWN, ROOM);

        List<DropDownResponseDTO> responseDTOS = roomRepository.fetchMinRoomByHospitalDepartmentId(hospitalDepartmentId);

        log.info(FETCHING_PROCESS_FOR_DROPDOWN_COMPLETED, ROOM, getDifferenceBetweenTwoTime(startTime));

        return responseDTOS;
    }

    private void validateRoomNumber(String roomNumber, Long count) {

        if (count.intValue() > 0) {
            log.error(ROOM_NUMBER_DUPLICATION_ERROR, roomNumber);
            throw new DataDuplicationException(
                    String.format(ROOM_NUMBER_DUPLICATION_MESSAGE, roomNumber));
        }
    }

    private void save(Room room) {
        roomRepository.save(room);
    }

    private Hospital fetchHospitalById(Long hospitalId) {
        return hospitalRepository.findActiveHospitalById(hospitalId)
                .orElseThrow(() -> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(hospitalId));
    }

    private Room fetchRoomById(Long id) {
        return roomRepository.fetchRoomById(id)
                .orElseThrow(() -> ROOM_WITH_GIVEN_ID_NOT_FOUND.apply(id));
    }

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(Hospital.class, "id", id.toString());
    };

    private Function<Long, NoContentFoundException> ROOM_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ROOM, id);
        throw new NoContentFoundException(Room.class, "id", id.toString());
    };
}
