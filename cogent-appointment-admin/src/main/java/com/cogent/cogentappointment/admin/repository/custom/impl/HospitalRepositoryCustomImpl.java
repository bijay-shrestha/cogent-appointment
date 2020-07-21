package com.cogent.cogentappointment.admin.repository.custom.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.appointmentServiceType.AppointmentServiceTypeDropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.*;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.commons.configuration.MinIOProperties;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.NO_RECORD_FOUND;
import static com.cogent.cogentappointment.admin.constants.QueryConstants.*;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.admin.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.admin.log.constants.HospitalLog.*;
import static com.cogent.cogentappointment.admin.query.CompanyQuery.*;
import static com.cogent.cogentappointment.admin.query.HospitalAppointmentServiceTypeQuery.QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE;
import static com.cogent.cogentappointment.admin.query.HospitalBillingModeInfoQuery.QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID;
import static com.cogent.cogentappointment.admin.query.HospitalQuery.*;
import static com.cogent.cogentappointment.admin.utils.CompanyUtils.parseToCompanyResponseDTO;
import static com.cogent.cogentappointment.admin.utils.commons.PageableUtils.addPagination;
import static com.cogent.cogentappointment.admin.utils.commons.QueryUtils.*;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class HospitalRepositoryCustomImpl implements HospitalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final MinIOProperties minIOProperties;

    public HospitalRepositoryCustomImpl(MinIOProperties minIOProperties) {
        this.minIOProperties = minIOProperties;
    }


    @Override
    public List<Object[]> validateHospitalDuplicity(String name, String esewaMerchantCode, String alias) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(ESEWA_MERCHANT_CODE, esewaMerchantCode)
                .setParameter(ALIAS, alias);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateCompanyDuplicity(String name, String code) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_COMPANY_DUPLICITY)
                .setParameter(NAME, name)
                .setParameter(CODE, code);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String esewaMerchantCode, String alias) {
        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(ESEWA_MERCHANT_CODE, esewaMerchantCode)
                .setParameter(ALIAS, alias);

        return query.getResultList();
    }

    @Override
    public List<Object[]> validateCompanyDuplicityForUpdate(Long id, String name, String code) {

        Query query = createQuery.apply(entityManager, QUERY_TO_VALIDATE_COMPANY_DUPLICITY_FOR_UPDATE)
                .setParameter(ID, id)
                .setParameter(NAME, name)
                .setParameter(CODE, code);

        return query.getResultList();
    }

    @Override
    public List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO,
                                                   Pageable pageable) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_HOSPITAL(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<HospitalMinimalResponseDTO> results =
                transformNativeQueryToResultList(query, HospitalMinimalResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    @Override
    public List<CompanyMinimalResponseDTO> searchCompany(CompanySearchRequestDTO searchRequestDTO, Pageable pageable) {

        Query query = createNativeQuery.apply(entityManager, QUERY_TO_SEARCH_COMPANY(searchRequestDTO));

        int totalItems = query.getResultList().size();

        addPagination.accept(pageable, query);

        List<CompanyMinimalResponseDTO> results = transformNativeQueryToResultList(query, CompanyMinimalResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        results.get(0).setTotalItems(totalItems);
        return results;
    }

    @Override
    public HospitalResponseDTO fetchDetailsById(Long id) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_DETAILS)
                .setParameter(ID, id)
                .setParameter(CDN_URL,minIOProperties.getCDN_URL());

        Query billingModeQuery = createQuery.apply(entityManager, QUERY_TO_GET_BILLING_MODE_DROP_DOWN_BY_HOSPITAL_ID)
                .setParameter(HOSPITAL_ID, id);

        try {
            HospitalResponseDTO hospitalDetails =
                    transformQueryToSingleResult(query, HospitalResponseDTO.class);

            hospitalDetails.setContactNumberResponseDTOS(fetchHospitalContactNumber(id));

            hospitalDetails.setHospitalAppointmentServiceTypeDetail(fetchHospitalAppointmentServiceType(id));

            hospitalDetails.setBillingMode(transformQueryToResultList(billingModeQuery, DropDownResponseDTO.class));

            return hospitalDetails;
        } catch (NoResultException ex) {
            throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        }
    }

    @Override
    public CompanyResponseDTO fetchCompanyDetailsById(Long id) {
        Query query = createNativeQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_DETAILS)
                .setParameter(ID, id);

        List<Object[]> results = query.getResultList();

        if (results.isEmpty()) throw HOSPITAL_WITH_GIVEN_ID_NOT_FOUND.apply(id);
        return parseToCompanyResponseDTO(results.get(0));
    }

    @Override
    public Integer fetchHospitalFollowUpCount(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_COUNT)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public Integer fetchHospitalFollowUpIntervalDays(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOLLOW_UP_INTERVAL_DAYS)
                .setParameter(HOSPITAL_ID, hospitalId);

        return (Integer) query.getSingleResult();
    }

    @Override
    public List<AppointmentServiceTypeDropDownResponseDTO> fetchAssignedAppointmentServiceType(Long hospitalId) {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ASSIGNED_APPOINTMENT_SERVICE_TYPE)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<AppointmentServiceTypeDropDownResponseDTO> appointmentServiceTypes =
                transformQueryToResultList(query, AppointmentServiceTypeDropDownResponseDTO.class);

        if (appointmentServiceTypes.isEmpty())
            throw HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND.get();

        else return appointmentServiceTypes;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    @Override
    public List<HospitalDropdownResponseDTO> fetchHospitalForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_FOR_DROPDOWN);

        List<HospitalDropdownResponseDTO> results = transformQueryToResultList(query, HospitalDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    @Override
    public List<CompanyDropdownResponseDTO> fetchActiveCompanyForDropDown() {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_COMPANY_FOR_DROPDOWN);

        List<CompanyDropdownResponseDTO> results = transformQueryToResultList(query, CompanyDropdownResponseDTO.class);

        if (results.isEmpty())
            throw HOSPITAL_NOT_FOUND.get();

        else return results;
    }

    private Supplier<NoContentFoundException> HOSPITAL_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT));
    };

    private Function<Long, NoContentFoundException> HOSPITAL_WITH_GIVEN_ID_NOT_FOUND = (id) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, HOSPITAL, id);
        throw new NoContentFoundException(String.format(NO_RECORD_FOUND, CLIENT), "id", id.toString());
    };

    private Supplier<NoContentFoundException> HOSPITAL_APPOINTMENT_SERVICE_TYPE_NOT_FOUND = () -> {
        log.error(CONTENT_NOT_FOUND, HOSPITAL_APPOINTMENT_SERVICE_TYPE);
        throw new NoContentFoundException(HospitalAppointmentServiceType.class);
    };

    private List<HospitalContactNumberResponseDTO> fetchHospitalContactNumber(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_CONTACT_NUMBER)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<HospitalContactNumberResponseDTO> contactNumbers =
                transformQueryToResultList(query, HospitalContactNumberResponseDTO.class);

        return contactNumbers.isEmpty() ? new ArrayList<>() : contactNumbers;
    }

    private List<HospitalAppointmentServiceTypeResponseDTO> fetchHospitalAppointmentServiceType(Long hospitalId) {
        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_HOSPITAL_APPOINTMENT_SERVICE_TYPE)
                .setParameter(HOSPITAL_ID, hospitalId);

        List<HospitalAppointmentServiceTypeResponseDTO> hospitalAppointmentServiceType =
                transformQueryToResultList(query, HospitalAppointmentServiceTypeResponseDTO.class);

        return hospitalAppointmentServiceType.isEmpty() ? new ArrayList<>() : hospitalAppointmentServiceType;
    }
}



