package com.cogent.cogentappointment.service;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.dto.response.department.DepartmentResponseDTO;
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

    List<DropDownResponseDTO> fetchDepartmentForDropdown();

    List<DropDownResponseDTO> fetchActiveDropDownList();

    List<DropDownResponseDTO> fetchDepartmentByHospitalId(Long hospitalId);
}
