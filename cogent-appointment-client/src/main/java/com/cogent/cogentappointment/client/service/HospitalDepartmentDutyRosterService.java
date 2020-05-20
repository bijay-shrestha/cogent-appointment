package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save.HospitalDepartmentDutyRosterRequestDTO;

/**
 * @author smriti on 20/05/20
 */
public interface HospitalDepartmentDutyRosterService {

    void save(HospitalDepartmentDutyRosterRequestDTO requestDTO);
}
