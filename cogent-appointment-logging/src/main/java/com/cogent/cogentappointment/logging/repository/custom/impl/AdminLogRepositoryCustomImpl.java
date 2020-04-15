package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogStaticsResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.UserMenuStaticsResponseDTO;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
import com.cogent.cogentappointment.logging.query.AdminLogQuery;
import com.cogent.cogentappointment.logging.repository.custom.AdminLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.logging.constants.QueryConstants.*;
import static com.cogent.cogentappointment.logging.query.AdminLogQuery.QUERY_TO_SEARCH_ADMIN_LOGS;
import static com.cogent.cogentappointment.logging.utils.common.PageableUtils.addPagination;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToResultList;

/**
 * @author Rupak
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminLogRepositoryCustomImpl implements AdminLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public AdminLogResponseDTO search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN_LOGS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());


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
    public UserMenuStaticsResponseDTO fetchUserMenuLogsStatics(AdminLogSearchRequestDTO searchRequestDTO) {

        Query query = createQuery.apply(entityManager, AdminLogQuery.QUERY_TO_FETCH_USER_LOGS_STATICS(searchRequestDTO))
                .setParameter(FROM_DATE, searchRequestDTO.getFromDate())
                .setParameter(TO_DATE, searchRequestDTO.getToDate());

        int totalItems = query.getResultList().size();

        List<AdminLogStaticsResponseDTO> result = transformQueryToResultList(query, AdminLogStaticsResponseDTO.class);

        if (result.isEmpty()) {
//            error();
            throw NO_USER_STATICS_FOUND.get();
        } else {
            UserMenuStaticsResponseDTO userMenuStaticsResponseDTO=new UserMenuStaticsResponseDTO();
            userMenuStaticsResponseDTO.setUserMenuCountList(result);
            userMenuStaticsResponseDTO.setTotalItems(totalItems);
            return userMenuStaticsResponseDTO;
        }
    }


    private Supplier<NoContentFoundException> NO_USER_STATICS_FOUND = () -> new NoContentFoundException(AdminLog.class);
}
