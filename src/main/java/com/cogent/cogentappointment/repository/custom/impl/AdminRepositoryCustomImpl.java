package com.cogent.cogentappointment.repository.custom.impl;

import com.cogent.cogentappointment.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.admin.*;
import com.cogent.cogentappointment.exception.NoContentFoundException;
import com.cogent.cogentappointment.model.Admin;
import com.cogent.cogentappointment.repository.custom.AdminRepositoryCustom;
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

import static com.cogent.cogentappointment.constants.ErrorMessageConstants.AdminServiceMessages;
import static com.cogent.cogentappointment.constants.ErrorMessageConstants.AdminServiceMessages.ADMIN_INFO_NOT_FOUND;
import static com.cogent.cogentappointment.constants.QueryConstants.*;
import static com.cogent.cogentappointment.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.query.AdminQuery.*;
import static com.cogent.cogentappointment.utils.AdminUtils.parseToAdminDetailResponseDTO;
import static com.cogent.cogentappointment.utils.AdminUtils.parseToAdminInfoByUsernameResponseDTO;
import static com.cogent.cogentappointment.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/21/19
 */
@Service
@Transactional(readOnly = true)
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> fetchAdminForValidation(String username, String email, String mobileNumber) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_FOR_VALIDATION)
                .setParameter(USERNAME, username)
                .setParameter(EMAIL, email)
                .setParameter(MOBILE_NUMBER, mobileNumber);

        return query.getResultList();
    }

    @Override
    public List<AdminDropdownDTO> fetchActiveAdminsForDropDown() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_ADMIN_FOR_DROPDOWN);

        List<AdminDropdownDTO> list = transformQueryToResultList(query, AdminDropdownDTO.class);

        if (list.isEmpty()) throw NO_ADMIN_FOUND.get();
        else return list;
    }

    @Override
    public List<AdminMinimalResponseDTO> search(AdminSearchRequestDTO searchRequestDTO, Pageable pageable) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminMinimalResponseDTO> result =
                transformNativeQueryToResultList(query, AdminMinimalResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_ADMIN_FOUND.get();
        else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        AdminDetailResponseDTO detailResponseDTO = fetchAdminDetailResponseDTO(id);

        if (detailResponseDTO.getHasMacBinding().equals(YES))
            detailResponseDTO.setMacAddressInfoResponseDTOS(getMacAddressInfo(id));

        return detailResponseDTO;
    }

    @Override
    public List<Object[]> fetchAdmin(AdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN)
                .setParameter(ID, updateRequestDTO.getId())
                .setParameter(EMAIL, updateRequestDTO.getEmail())
                .setParameter(MOBILE_NUMBER, updateRequestDTO.getMobileNumber());

        return query.getResultList();
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
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_INFO)
                .setParameter(USERNAME, requestDTO.getUsername())
                .setParameter(EMAIL, requestDTO.getUsername())
                .setParameter(CODE, requestDTO.getSubDepartmentCode());

        try {
            return transformQueryToSingleResult(query, AdminLoggedInInfoResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(ADMIN_INFO_NOT_FOUND);
        }
    }

    @Override
    public AdminInfoByUsernameResponseDTO fetchAdminInfoByUsername(String username) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_INFO_BY_USERNAME)
                .setParameter(USERNAME, username)
                .setParameter(EMAIL, username);

        List<Object[]> results = query.getResultList();

        return results.isEmpty() ? null : parseToAdminInfoByUsernameResponseDTO(results.get(0));
    }

    @Override
    public List<AdminSubDepartmentResponseDTO> fetchLoggedInAdminSubDepartmentList(String username) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_LOGGED_IN_ADMIN_SUB_DEPARTMENT_LIST)
                .setParameter(USERNAME, username);

        return transformQueryToResultList(query, AdminSubDepartmentResponseDTO.class);
    }

    public AdminDetailResponseDTO fetchAdminDetailResponseDTO(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_DETAIL)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return parseToAdminDetailResponseDTO.apply(results.get(0));
    }

    public List<MacAddressInfoResponseDTO> getMacAddressInfo(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_FO_FETCH_MAC_ADDRESS_INFO)
                .setParameter(ID, id);

        return transformQueryToResultList(query, MacAddressInfoResponseDTO.class);
    }

    private Supplier<NoContentFoundException> NO_ADMIN_FOUND = () -> new NoContentFoundException(Admin.class);

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(Admin.class, "id", id.toString());
    };

    private Function<String, NoContentFoundException> ADMIN_NOT_FOUND = (username) -> {
        throw new NoContentFoundException(String.format(AdminServiceMessages.ADMIN_NOT_FOUND, username),
                "username/email", username);
    };
}

