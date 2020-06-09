package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.ChargeRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.ChargeResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentMinimalResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDepartment.HospitalDepartmentResponseDTO;
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

    List<DropDownResponseDTO> fetchAvailableRoom();

    HospitalDepartmentMinimalResponseDTO search(HospitalDepartmentSearchRequestDTO departmentSearchRequestDTO,
                                                Pageable pageable);

    HospitalDepartmentResponseDTO fetchHospitalDepartmentDetails(Long id);

    void delete(DeleteRequestDTO deleteRequestDTO);

    ChargeResponseDTO fetchAppointmentCharge(ChargeRequestDTO requestDTO);

    List<DoctorDropdownDTO> fetchAssignedHospitalDepartmentDoctor(Long hospitalDepartmentId);

}
