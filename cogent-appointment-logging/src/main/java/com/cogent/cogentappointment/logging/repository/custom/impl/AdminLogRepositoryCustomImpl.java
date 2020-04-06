package com.cogent.cogentappointment.logging.repository.custom.impl;

import com.cogent.cogentappointment.logging.dto.request.admin.AdminLogSearchRequestDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogResponseDTO;
import com.cogent.cogentappointment.logging.dto.response.AdminLogSearchResponseDTO;
import com.cogent.cogentappointment.logging.exception.NoContentFoundException;
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
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN_LOGS(searchRequestDTO));

        addPagination.accept(pageable, query);

        List<AdminLogSearchResponseDTO> result = transformQueryToResultList(query, AdminLogSearchResponseDTO.class);

        int totalItems = query.getResultList().size();

        if (ObjectUtils.isEmpty(result)) {
//            error()//Error not integrated...
            throw new NoContentFoundException(AdminLog.class);
        } else {

            AdminLogResponseDTO adminLogResponseDTO=new AdminLogResponseDTO();
            adminLogResponseDTO.setResponseDTOList(result);
            adminLogResponseDTO.setTotalItems(totalItems);

            return adminLogResponseDTO;

        }
    }
}
