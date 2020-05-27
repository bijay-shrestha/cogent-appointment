package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.response.room.RoomMinimalResponse;
import com.cogent.cogentappointment.client.dto.response.room.RoomMinimalResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.RoomRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.constants.RoomLog.ROOM;
import static com.cogent.cogentappointment.client.query.RoomQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.client.utils.commons.SecurityContextUtils.getLoggedInHospitalId;

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
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ROOM.apply(searchRequestDTO))
                .setParameter(HOSPITAL_ID,getLoggedInHospitalId());

        RoomMinimalResponseDTO response=new RoomMinimalResponseDTO();

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
