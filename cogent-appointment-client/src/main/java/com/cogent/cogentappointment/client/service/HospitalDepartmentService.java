package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentService {

    void save(HospitalDepartmentRequestDTO requestDTO);

    void update(HospitalDepartmentUpdateRequestDTO requestDTO);

    List<DropDownResponseDTO> fetchMinHospitalDepartment();

    List<DropDownResponseDTO> fetchActiveMinHospitalDepartment();

    HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO departmentSearchRequestDTO,
                                                      Pageable pageable);
}
