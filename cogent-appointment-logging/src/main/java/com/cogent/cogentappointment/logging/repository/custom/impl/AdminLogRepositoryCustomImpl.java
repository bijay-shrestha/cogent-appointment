package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.repository.custom.AdminLogRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static com.cogent.cogentappointment.logging.constants.QueryConstants.DATE;
import static com.cogent.cogentappointment.logging.query.AdminLogQuery.QUERY_TO_SEARCH_ADMIN_LOGS;
import static com.cogent.cogentappointment.logging.utils.common.DateUtils.utilDateToSqlDate;
import static com.cogent.cogentappointment.logging.utils.common.PageableUtils.addPagination;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.createQuery;
import static com.cogent.cogentappointment.logging.utils.common.QueryUtils.transformQueryToResultList;

/**
 * @author Rupak
 */
@Repository
@Transactional(readOnly = true)
public class AdminLogRepositoryCustomImpl implements AdminLogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AdminLogSearchResponseDTO> search(AdminLogSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN_LOGS(searchRequestDTO))
                .setParameter(DATE, utilDateToSqlDate(searchRequestDTO.getDate()));

        addPagination.accept(pageable, query);

        List<AdminLogSearchResponseDTO> result = transformQueryToResultList(query, AdminLogSearchResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            error();
            throw NO_ADMIN_LOGS_FOUND.get();
        } else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }
}
