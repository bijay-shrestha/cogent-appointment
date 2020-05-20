package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentRequestDTO;
import com.cogent.cogentappointment.client.dto.request.hospitalDepartment.HospitalDepartmentUpdateRequestDTO;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentService {

    void save(HospitalDepartmentRequestDTO requestDTO);

    void update(HospitalDepartmentUpdateRequestDTO requestDTO);
}
