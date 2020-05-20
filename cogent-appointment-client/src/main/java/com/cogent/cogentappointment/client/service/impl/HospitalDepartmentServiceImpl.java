package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.repository.HospitalDepartmentRepository;
import com.cogent.cogentappointment.client.service.HospitalDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Sauravi Thapa ON 5/20/20
 */

@Service
@Transactional
@Slf4j
public class HospitalDepartmentServiceImpl implements HospitalDepartmentService {

    private final HospitalDepartmentRepository hospitalDepartmentRepository;

    public HospitalDepartmentServiceImpl(HospitalDepartmentRepository hospitalDepartmentRepository) {
        this.hospitalDepartmentRepository = hospitalDepartmentRepository;
    }
}
