package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
import com.cogent.cogentappointment.logging.repository.custom.ClientLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.logging.constants.QueryConstants.*;
import static com.cogent.cogentappointment.logging.query.ClientLogQuery.QUERY_TO_FETCH_USER_LOGS_STATICS;
import static com.cogent.cogentappointment.logging.query.ClientLogQuery.QUERY_TO_SEARCH_CLIENT_LOGS;
import static com.cogent.cogentappointment.logging.utils.common.PageableUtils.addPagination;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToResultList;
import static com.cogent.cogentappointment.logging.utils.common.SecurityContextUtils.getLoggedInCompanyId;

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
    public AdminLogResponseDTO search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable) {

        Long companyId = getLoggedInCompanyId();
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_CLIENT_LOGS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(USERNAME, searchRequestDTO.getUserName())
                .setParameter(HOSPITAL_ID, companyId);


        addPagination.accept(pageable, query);

        List<AdminLogSearchResponseDTO> result = transformQueryToResultList(query, AdminLogSearchResponseDTO.class);

        int totalItems = query.getResultList().size();

        if (ObjectUtils.isEmpty(result)) {
//            error()//Error not integrated...
            throw new NoContentFoundException(AdminLog.class);
        } else {

            AdminLogResponseDTO adminLogResponseDTO = new AdminLogResponseDTO();
            adminLogResponseDTO.setUserLogList(result);
            adminLogResponseDTO.setTotalItems(totalItems);

            return adminLogResponseDTO;
        }
    }

    @Override
    public List<AdminLogStaticsResponseDTO> fetchUserMenuLogsStatics(ClientLogSearchRequestDTO searchRequestDTO) {

        Long companyId = getLoggedInCompanyId();

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_USER_LOGS_STATICS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate())
                .setParameter(USERNAME, searchRequestDTO.getUserName())
                .setParameter(HOSPITAL_ID, companyId);


        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        return result;
    }
}
