package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.response.dashboard.DashboardFeatureResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.DashBoardQuery;
import com.cogent.cogentappointment.admin.repository.DashboardFeatureRepository;
import com.cogent.cogentappointment.admin.repository.custom.AdminDashboardRepositoryCustom;
import com.cogent.cogentappointment.admin.utils.commons.DashboardFeatureUtils;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.query.DashBoardQuery.QUERY_TO_FETCH_DASHBOARD_FEATURES;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author Rupak
 */
public class DashboardFeatureRepositoryCustomImpl implements AdminDashboardRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DashboardFeatureResponseDTO> fetchActiveDashboardFeatureByAdmin(Long adminId, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DASHBOARD_FEATURES(adminId, hospitalId));

        List<DashboardFeatureResponseDTO> result = transformQueryToResultList(query, DashboardFeatureResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_DASHBOARD_FEATURE_FOUND.get();
        else {
            return result;
        }
    }

    private Supplier<NoContentFoundException> NO_DASHBOARD_FEATURE_FOUND = () -> new NoContentFoundException(DashboardFeature.class);

}
