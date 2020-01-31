package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.constants.QueryConstants;
import com.cogent.cogentappointment.client.constants.StatusConstants;
import com.cogent.cogentappointment.client.dto.request.admin.AdminInfoRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.admin.AdminUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.admin.*;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.model.Admin;
import com.cogent.cogentappointment.client.repository.custom.AdminRepositoryCustom;
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
import static com.cogent.cogentappointment.client.query.AdminQuery.*;
import static com.cogent.cogentappointment.client.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.*;

/**
 * @author smriti on 7/21/19
 */
@Service
@Transactional(readOnly = true)
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> validateDuplicity(String username, String email, String mobileNumber,
                                            Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_FOR_VALIDATION)
                .setParameter(QueryConstants.USERNAME, username)
                .setParameter(QueryConstants.EMAIL, email)
                .setParameter(QueryConstants.MOBILE_NUMBER, mobileNumber)
                .setParameter(QueryConstants.HOSPITAL_ID, hospitalId);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateDuplicity(AdminUpdateRequestDTO updateRequestDTO) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FIND_ADMIN_EXCEPT_CURRENT_ADMIN)
                .setParameter(QueryConstants.ID, updateRequestDTO.getId())
                .setParameter(QueryConstants.EMAIL, updateRequestDTO.getEmail())
                .setParameter(QueryConstants.MOBILE_NUMBER, updateRequestDTO.getMobileNumber())
                .setParameter(QueryConstants.HOSPITAL_ID, updateRequestDTO.getHospitalId());

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
        Query query = createQuery.apply(entityManager, QUERY_TO_SEARCH_ADMIN(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<AdminMinimalResponseDTO> result = transformQueryToResultList(query, AdminMinimalResponseDTO.class);

        if (ObjectUtils.isEmpty(result)) throw NO_ADMIN_FOUND.get();
        else {
            result.get(0).setTotalItems(totalItems);
            return result;
        }
    }

    @Override
    public AdminDetailResponseDTO fetchDetailsById(Long id) {
        AdminDetailResponseDTO detailResponseDTO = fetchAdminDetailResponseDTO(id);

        if (detailResponseDTO.getHasMacBinding().equals(StatusConstants.YES))
            detailResponseDTO.setAdminMacAddressInfo(getMacAddressInfo(id));

        return detailResponseDTO;
    }


    @Override
    public Admin fetchAdminByUsernameOrEmail(String username) {
        try {
            return entityManager.createQuery(QUERY_TO_FETCH_ADMIN_BY_USERNAME_OR_EMAIL, Admin.class)
                    .setParameter(QueryConstants.USERNAME, username)
                    .setParameter(QueryConstants.EMAIL, username)
                    .getSingleResult();
        } catch (NoResultException ex) {
            throw ADMIN_NOT_FOUND.apply(username);
        }
    }

    @Override
    public AdminLoggedInInfoResponseDTO fetchLoggedInAdminInfo(AdminInfoRequestDTO requestDTO) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_INFO)
                .setParameter(QueryConstants.USERNAME, requestDTO.getUsername())
                .setParameter(QueryConstants.EMAIL, requestDTO.getUsername());

        try {
            return transformQueryToSingleResult(query, AdminLoggedInInfoResponseDTO.class);
        } catch (NoResultException e) {
            throw new NoContentFoundException(ADMIN_INFO_NOT_FOUND);
        }
    }

    public AdminDetailResponseDTO fetchAdminDetailResponseDTO(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ADMIN_DETAIL)
                .setParameter(QueryConstants.ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty())
            throw ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(id);

        return transformQueryToResultList(query, AdminDetailResponseDTO.class).get(0);
    }

    public List<AdminMacAddressInfoResponseDTO> getMacAddressInfo(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_FO_FETCH_MAC_ADDRESS_INFO)
                .setParameter(QueryConstants.ID, id);

        return transformQueryToResultList(query, AdminMacAddressInfoResponseDTO.class);
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

