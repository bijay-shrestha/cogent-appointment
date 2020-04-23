package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
import com.cogent.cogentappointment.logging.repository.custom.ClientLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.ClientLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.logging.constants.QueryConstants.*;
import static com.cogent.cogentappointment.logging.query.ClientLogQuery.*;
import static com.cogent.cogentappointment.logging.utils.common.PageableUtils.addPagination;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToResultList;

/**
 * @author Rupak
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class ClientLogRepositoryCustomImpl implements ClientLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserMenuLogResponseDTO searchByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_CLIENT_LOGS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<UserMenuLogSearchResponseDTO> result = transformQueryToResultList(query, UserMenuLogSearchResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
//            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            UserMenuLogResponseDTO userMenuLogResponseDTO = new UserMenuLogResponseDTO();
            userMenuLogResponseDTO.setUserLogList(result);
            userMenuLogResponseDTO.setTotalItems(totalItems);

            return userMenuLogResponseDTO;
        }
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsByClientId(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            //            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            Long totalCount = result.stream().mapToLong(AdminLogStaticsResponseDTO::getCount).sum();

            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO = new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalCount(totalCount);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }

    @Override
    public UserMenuLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Long hospitalId, Pageable pageable) {

        searchRequestDTO.setClientId(hospitalId);
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_CLIENT_LOGS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<UserMenuLogSearchResponseDTO> result = transformQueryToResultList(query, UserMenuLogSearchResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
//            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            UserMenuLogResponseDTO userMenuLogResponseDTO = new UserMenuLogResponseDTO();
            userMenuLogResponseDTO.setUserLogList(result);
            userMenuLogResponseDTO.setTotalItems(totalItems);

            return userMenuLogResponseDTO;
        }
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Long hospitalId) {

        searchRequestDTO.setClientId(hospitalId);
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            //            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            Long totalCount = result.stream().mapToLong(AdminLogStaticsResponseDTO::getCount).sum();

            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO = new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalCount(totalCount);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagram(ClientLogSearchRequestDTO searchRequestDTO, Long hospitalId) {

        searchRequestDTO.setClientId(hospitalId);
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS_FOR_PIE_CHART(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            //            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            Long totalCount = result.stream().mapToLong(AdminLogStaticsResponseDTO::getCount).sum();

            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO = new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalCount(totalCount);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStaticsforDiagramByClientId(ClientLogSearchRequestDTO searchRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS_FOR_PIE_CHART(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setMaxResults(10);

        int totalItems = query.getResultList().size();

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            //            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            Long totalCount = result.stream().mapToLong(AdminLogStaticsResponseDTO::getCount).sum();

            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO = new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalCount(totalCount);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }

    @Override
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO, Long hospitalId, Pageable pageable) {

        searchRequestDTO.setClientId(hospitalId);
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            //            error()//Error not integrated...
            throw NO_CLIENT_LOGS_FOUND.get();
        } else {

            Long totalCount = result.stream().mapToLong(AdminLogStaticsResponseDTO::getCount).sum();

            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO = new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalCount(totalCount);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }


    private Supplier<NoContentFoundException> NO_CLIENT_LOGS_FOUND = () -> new NoContentFoundException(ClientLog.class);
}
