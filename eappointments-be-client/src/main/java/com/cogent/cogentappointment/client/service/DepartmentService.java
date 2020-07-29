package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.department.DepartmentResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author smriti ON 25/01/2020
 */
public interface DepartmentService {

    void save(DepartmentRequestDTO departmentRequestDto);

    void update(DepartmentUpdateRequestDTO departmentUpdateRequestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DepartmentMinimalResponseDTO> search(DepartmentSearchRequestDTO departmentSearchRequestDTO, Pageable pageable);

    DepartmentResponseDTO fetchDetails(Long id);

    List<DropDownResponseDTO> fetchMinDepartment();

    List<DropDownResponseDTO> fetchActiveMinDepartment();
}
