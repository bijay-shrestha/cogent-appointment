package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.CompanyAdmin.CompanyAdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.admin.*;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminLoggedInInfoResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyAdmin.CompanyAdminMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.dashboard.DashboardFeatureResponseDTO;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.query.DashBoardQuery;
import com.cogent.cogentappointment.admin.repository.custom.AdminRepositoryCustom;
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

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.AdminServiceMessages.ADMIN_INFO_NOT_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN_NOT_FOUND_ERROR;
import static com.cogent.cogentappointment.admin.query.AdminQuery.QUERY_FO_FETCH_MAC_ADDRESS_INFO;
import static com.cogent.cogentappointment.admin.query.AdminQuery.*;
import static com.cogent.cogentappointment.admin.query.AdminQuery.QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL;
import static com.cogent.cogentappointment.admin.query.CompanyAdminQuery.*;
import static com.cogent.cogentappointment.admin.query.DashBoardQuery.QUERY_TO_FETCH_DASHBOARD_FEATURES;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

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
    public List<Object[]> validateDuplicity(String email, String mobileNumber,
                                            Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_FOR_VALIDATION)
                .setParameter(EMAIL, email)
                .setParameter(MOBILE_NUMBER, mobileNumber)
                .setParameter(HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicityForCompanyAdmin(String email, String mobileNumber) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_COMPANY_ADMIN_FOR_VALIDATION)
                .setParameter(EMAIL, email)
                .setParameter(MOBILE_NUMBER, mobileNumber);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN)
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(EMAIL, updateRequestDTO.getEmail())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber())
                .setParameter(HOSPITAL_ID, updateRequestDTO.getHospitalId());

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateCompanyAdminDuplicity(CompanyAdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_COMPANY_ADMIN_EXCEPT_CURRENT_COMPANY_ADMIN)
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(EMAIL, updateRequestDTO.getEmail())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber())
                .setParameter(COMPANY_ID, updateRequestDTO.getCompanyId());

        return query.getResultList();
    }

    @Override
    public List<AdminDropdownDTO> fetchActiveAdminsForDropDown() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN);

        List<AdminDropdownDTO> list = transformQueryToResultList(query, AdminDropdownDTO.class);

        if (list.isEmpty()) {
            error();
            throw NO_ADMIN_FOUND.get();
        } else return list;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN(searchRequestDTO));

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
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        AdminDetailResponseDTO detailResponseDTO = fetchAdminDetailResponseDTO(id);

        if (detailResponseDTO.getHasMacBinding().equals(YES))
            detailResponseDTO.setAdminMacAddressInfo(getMacAddressInfo(id));

        return detailResponseDTO;
    }

    @Override
    public Admin fetchAdminByUsernameOrEmail(String username) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL, Admin.class)
                    .setParameter(USERNAME, username)
                    .setParameter(EMAIL, username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ADMIN_NOT_FOUND.apply(username);
        }
    }



    @Override
    public LoggedInAdminDTO getLoggedInAdmin(String email) {
        Query query = createQuery.apply(entityManager, QUERY_TO_GET_LOGGED_COMPANY_ADMIN_INFO)
                .setParameter(EMAIL, email);
        try {
            return transformQueryToSingleResult(query, LoggedInAdminDTO.class);
        } catch (NoResultException e) {
            throw ADMIN_NOT_FOUND.apply(email);
        }
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveCompanyAdminsForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_COMPANY_ADMIN_FOR_DROPDOWN);

        List<DropDownResponseDTO> list = transformQueryToResultList(query, DropDownResponseDTO.class);

        if (list.isEmpty()){
            error();
            throw NO_ADMIN_FOUND.get();
        }
        else return list;
    }

    @Override
    public List<CompanyAdminMinimalResponseDTO> searchCompanyAdmin(CompanyAdminSearchRequestDTO searchRequestDTO,
                                                                   Pageable pageable) {
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_COMPANY_ADMIN(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<CompanyAdminMinimalResponseDTO> result = transformQueryToResultList(query, CompanyAdminMinimalResponseDTO.class);

        if (ObjectUtils.isEmpty(result)){
            error();
            throw NO_ADMIN_FOUND.get();
        }
        else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }

    @Override
    public CompanyAdminDetailResponseDTO fetchCompanyAdminDetailsById(Long id) {
        CompanyAdminDetailResponseDTO detailResponseDTO = fetchCompanyAdminDetailResponseDTO(id);

        if (detailResponseDTO.getHasMacBinding().equals(YES))
            detailResponseDTO.setAdminMacAddressInfo(getMacAddressInfo(id));

        return detailResponseDTO;
    }

    @Override
    public CompanyAdminLoggedInInfoResponseDTO fetchLoggedInCompanyAdminInfo(CompanyAdminInfoRequestDTO requestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_ADMIN_INFO)
                .setParameter(EMAIL, requestDTO.getEmail());

        try {
            return transformQueryToSingleResult(query, CompanyAdminLoggedInInfoResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(ADMIN_INFO_NOT_FOUND);
        }
    }

    @Override
    public List<DashboardFeatureResponseDTO> fetchDashboardFeaturesByAdmin(Long adminId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_DASHBOARD_FEATURES(adminId));

        List<DashboardFeatureResponseDTO> result = transformQueryToResultList(query, DashboardFeatureResponseDTO.class);

        if (ObjectUtils.isEmpty(result)){
            log.error(CONTENT_NOT_FOUND,DashboardFeature.class.getSimpleName());
            throw NO_DASHBOARD_FEATURE_FOUND.get();
        }
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

    private AdminDetailResponseDTO fetchAdminDetailResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_DETAIL)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) {
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }

        return transformQueryToResultList(query, AdminDetailResponseDTO.class).get(0);
    }

    private CompanyAdminDetailResponseDTO fetchCompanyAdminDetailResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_ADMIN_DETAIL)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return transformQueryToResultList(query, CompanyAdminDetailResponseDTO.class).get(0);
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

