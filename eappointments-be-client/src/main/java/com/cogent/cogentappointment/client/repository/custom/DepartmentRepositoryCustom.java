package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentResponseDTO;
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

    List<Object[]> validateDuplicity(DepartmentRequestDTO requestDTO, Long hospitalId);

    List<Object[]> validateDuplicity(DepartmentUpdateRequestDTO requestDTO, Long hospitalId);

    List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO searchRequestDTO,
                                              Long hospitalId,
                                              Pageable pageable);

    DepartmentResponseDTO fetchDetails(Long id, Long hospitalId);

    Optional<List<DropDownResponseDTO>> fetchMinDepartment(Long hospitalId);

    Optional<List<DropDownResponseDTO>> fetchActiveMinDepartment(Long hospitalId);

}
