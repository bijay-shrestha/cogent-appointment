package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.dashboard.DashboardFeatureResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.custom.AdminDashboardRepositoryCustom;
import com.cogent.cogentappointment.client.repository.custom.DashboardFeatureRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_FETCH_DASHBOARD_FEATURES;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_VALIDATE_DASHBOARD_FEATURE_COUNT;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Rupak
 */
@Repository
@Transactional(readOnly = true)
public class DashboardFeatureRepositoryCustomImpl implements AdminDashboardRepositoryCustom, DashboardFeatureRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DashboardFeatureResponseDTO> fetchActiveDashboardFeatureByAdmin(Long adminId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DASHBOARD_FEATURES(adminId));

        List<DashboardFeatureResponseDTO> result = transformQueryToResultList(query, DashboardFeatureResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_DASHBOARD_FEATURE_FOUND.get();
        else {
            return result;
        }
    }

    private Supplier<NoContentFoundException> NO_DASHBOARD_FEATURE_FOUND = () -> new NoContentFoundException(DashboardFeature.class);

    @Override
    public List<DashboardFeature> validateDashboardFeatureCount(String ids) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DASHBOARD_FEATURE_COUNT(ids));

        return query.getResultList();
    }
}
