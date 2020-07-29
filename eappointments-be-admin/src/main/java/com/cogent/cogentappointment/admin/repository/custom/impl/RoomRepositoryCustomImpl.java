package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.room.RoomMinimalResponse;
import com.cogent.cogentappointment.admin.dto.response.room.RoomMinimalResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.RoomRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.constants.RoomLog.ROOM;
import static com.cogent.cogentappointment.admin.query.RoomQuery.*;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Sauravi Thapa ON 5/19/20
 */

@Repository
@Transactional(readOnly = true)
@Slf4j
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long validateDuplicity(String roomNumber, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(ROOM_NUMBER, roomNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public Long validateDuplicity(Long id, String roomNumber, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(ROOM_NUMBER, roomNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinRoom(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ROOM_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw ROOM_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinRoom(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROOM_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw ROOM_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public RoomMinimalResponseDTO search(RoomSearchRequestDTO searchRequestDTO, Pageable pageable) {

        RoomMinimalResponseDTO response = new RoomMinimalResponseDTO();

        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ROOM.apply(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<RoomMinimalResponse> results = transformQueryToResultList(
                query, RoomMinimalResponse.class);

        if (results.isEmpty()) {
            error();
            throw ROOM_NOT_FOUND.get();
        } else {
            response.setResponse(results);
            response.setTotalItems(totalItems);
            return response;
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinRoomByHospitalDepartmentId(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ROOM_FOR_DROPDOWN_BY_HOSPITAL_DEPARTMENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw ROOM_NOT_FOUND.get();
        } else return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinRoomForAppointmentStatus(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROOM_LIST_FOR_APPOINTMENT_STATUS)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        return results;
    }

    @Override
    public List<DropDownResponseDTO> fetchMinRoomByHospitalDepartmentId(Long hospitalDepartmentId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ROOM_FOR_DROPDOWN_BY_HOSPITAL_DEPARTMENT_ID)
                .setParameter(HOSPITAL_DEPARTMENT_ID, hospitalDepartmentId);

        List<DropDownResponseDTO> results = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (results.isEmpty()) {
            error();
            throw ROOM_NOT_FOUND.get();
        } else return results;
    }

    private Supplier<NoContentFoundException> ROOM_NOT_FOUND = () ->
            new NoContentFoundException(Room.class);

    private void error() {
        log.error(CONTENT_NOT_FOUND, ROOM);
    }
}
