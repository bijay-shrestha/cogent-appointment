package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.company.CompanySearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospital.HospitalSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.company.CompanyResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalDropdownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospital.HospitalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
@Qualifier("hospitalRepositoryCustom")
public interface HospitalRepositoryCustom {

    List<Object[]> validateHospitalDuplicity(String name, String code);

    List<Object[]> validateHospitalDuplicityForUpdate(Long id, String name, String code);

    List<HospitalMinimalResponseDTO> search(HospitalSearchRequestDTO searchRequestDTO, Pageable pageable);

    List<CompanyMinimalResponseDTO> searchCompany(CompanySearchRequestDTO searchRequestDTO, Pageable pageable);

    List<HospitalDropdownResponseDTO> fetchActiveHospitalForDropDown();

    List<CompanyDropdownResponseDTO> fetchActiveCompanyForDropDown();

    HospitalResponseDTO fetchDetailsById(Long id);

    CompanyResponseDTO fetchCompanyDetailsById(Long id);

    Integer fetchHospitalFollowUpCount(Long hospitalId);

    Integer fetchHospitalFollowUpIntervalDays(Long hospitalId);
}
