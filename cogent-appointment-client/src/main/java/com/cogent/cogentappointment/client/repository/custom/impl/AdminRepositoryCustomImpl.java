package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.*;
import com.cogent.cogentappointment.client.dto.response.dashboard.DashboardFeatureResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.query.DashBoardQuery;
import com.cogent.cogentappointment.client.repository.custom.AdminRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.DashboardFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages;
import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.AdminServiceMessages.ADMIN_INFO_NOT_FOUND;
import static com.cogent.cogentappointment.client.constants.QueryConstants.*;
import static com.cogent.cogentappointment.client.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.ADMIN;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.ADMIN_NOT_FOUND_ERROR;
import static com.cogent.cogentappointment.client.query.AdminQuery.*;
import static com.cogent.cogentappointment.client.query.DashBoardQuery.QUERY_TO_FETCH_DASHBOARD_FEATURES;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/21/19
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Object[] validateAdminCount(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_ADMIN_COUNT)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        return results.get(0);
    }

    @Override
    public List<Object[]> validateDuplicity(String email, String mobileNumber) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_FOR_VALIDATION)
                .setParameter(EMAIL, email)
                .setParameter(MOBILE_NUMBER, mobileNumber);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN)
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(EMAIL, updateRequestDTO.getEmail())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber());
        return query.getResultList();
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinAdmin(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<DropDownResponseDTO> list = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw NO_ADMIN_FOUND.get();
        } else return list;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO,
                                                Long hospitalId,
                                                Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN(searchRequestDTO))
                .setParameter(HOSPITAL_ID, hospitalId);

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminMinimalResponseDTO> result = transformQueryToResultList(query, AdminMinimalResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) {
            error();
            throw NO_ADMIN_FOUND.get();
        } else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id, Long hospitalId) {
        AdminDetailResponseDTO detailResponseDTO = fetchAdminDetailResponseDTO(id, hospitalId);

        if (detailResponseDTO.getHasMacBinding().equals(YES))
            detailResponseDTO.setAdminMacAddressInfo(getMacAddressInfo(id));

        return detailResponseDTO;
    }


    @Override
    public Admin fetchAdminByEmail(String email) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_ADMIN_BY_EMAIL, Admin.class)
                    .setParameter(EMAIL, email)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ADMIN_NOT_FOUND.apply(email);
        }
    }

    @Override
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO, Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_INFO)
                .setParameter(EMAIL, requestDTO.getEmail())
                .setParameter(HOSPITAL_ID, hospitalId);

        try {
            return transformQueryToSingleResult(query, AdminLoggedInInfoResponseDTO.class);
        } catch (NoResultException e) {
            log.error(ADMIN_INFO_NOT_FOUND);
            throw new NoContentFoundException(ADMIN_INFO_NOT_FOUND);
        }
    }

    @Override
    public List<DashboardFeatureResponseDTO> fetchDashboardFeaturesByAdmin(Long adminId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DASHBOARD_FEATURES(adminId));

        List<DashboardFeatureResponseDTO> result = transformQueryToResultList(query, DashboardFeatureResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_DASHBOARD_FEATURE_FOUND.get();
        else {
            return result;
        }
    }

    @Override
    public List<DashboardFeatureResponseDTO> fetchOverAllDashboardFeature() {
        Query query = createQuery.apply(entityManager, DashBoardQuery.QUERY_TO_FETCH_DASHBOARD_FEATURES);

        List<DashboardFeatureResponseDTO> result = transformQueryToResultList(query, DashboardFeatureResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_DASHBOARD_FEATURE_FOUND.get();
        else {
            return result;
        }
    }

    @Override
    public LoggedInAdminDTO getLoggedInAdmin(String email) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LOGGED_ADMIN_INFO)
                .setParameter(EMAIL, email);
        try {
            return transformQueryToSingleResult(query, LoggedInAdminDTO.class);
        } catch (NoResultException e) {
            throw ADMIN_NOT_FOUND.apply(email);
        }
    }

    private AdminDetailResponseDTO fetchAdminDetailResponseDTO(Long id, Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_DETAIL)
                .setParameter(ID, id)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return transformQueryToResultList(query, AdminDetailResponseDTO.class).get(0);
    }

    private List<AdminMacAddressInfoResponseDTO> getMacAddressInfo(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_FO_FETCH_MAC_ADDRESS_INFO)
                .setParameter(ID, id);

        return transformQueryToResultList(query, AdminMacAddressInfoResponseDTO.class);
    }

    private Supplier<NoContentFoundException> NO_DASHBOARD_FEATURE_FOUND = () -> new NoContentFoundException(DashboardFeature.class);

    private Supplier<NoContentFoundException> NO_ADMIN_FOUND = () -> new NoContentFoundException(Admin.class);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ADMIN, id);
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Function<String, NoContentFoundException> ADMIN_NOT_FOUND = (email) -> {
        log.error(ADMIN_NOT_FOUND_ERROR, email);
        throw new NoContentFoundException(String.format(AdminServiceMessages.ADMIN_NOT_FOUND, email),
                "email", email);
    };

    private void error() {
        log.error(CONTENT_NOT_FOUND, ADMIN);
    }
}

