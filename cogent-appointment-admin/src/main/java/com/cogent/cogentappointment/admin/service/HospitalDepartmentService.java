package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.ChargeResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentService {

    void save(HospitalDepartmentRequestDTO requestDTO);

    void update(HospitalDepartmentUpdateRequestDTO requestDTO);

    List<DropDownResponseDTO> fetchMinHospitalDepartment(Long hospitalId);

    List<DropDownResponseDTO> fetchActiveMinHospitalDepartment(Long hospitalId);

    List<DropDownResponseDTO> fetchAvailableRoom(Long hospitalId);

    HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO departmentSearchRequestDTO,
                                                Pageable pageable);

    HospitalDepartmentResponseDTO fetchHospitalDepartmentDetails(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO );


}
