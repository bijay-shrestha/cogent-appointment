package com.cogent.cogentappointment.repository.custom;

import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author smriti ON 25/01/2020
 */
@Repository
@Qualifier("departmentRepositoryCustom")
public interface DepartmentRepositoryCustom {

    List<Object[]> validateDuplicity(DepartmentRequestDTO requestDTO);

    List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO);

    List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO, Pageable pageable);

    DepartmentResponseDTO fetchDetails(Long id);

    Optional<List<DropDownResponseDTO>> fetchDepartmentForDropdown();

    Optional<List<DropDownResponseDTO>> fetchActiveDropDownList();
}
