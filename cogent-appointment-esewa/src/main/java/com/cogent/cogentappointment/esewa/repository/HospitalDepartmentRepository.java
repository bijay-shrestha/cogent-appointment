package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author smriti on 28/05/20
 */
public interface HospitalDepartmentRepository extends JpaRepository<HospitalDepartment, Long>,
        HospitalDepartmentRepositoryCustom {


}
