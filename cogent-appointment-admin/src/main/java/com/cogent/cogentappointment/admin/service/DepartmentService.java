package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.department.DepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.department.DepartmentResponseDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
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
