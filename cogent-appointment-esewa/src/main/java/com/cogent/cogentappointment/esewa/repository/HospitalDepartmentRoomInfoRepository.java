package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/02/20
 */
@Repository
public interface HospitalDepartmentRoomInfoRepository extends JpaRepository<HospitalDepartmentRoomInfo, Long>,
        HospitalDepartmentRoomInfoRepositoryCustom {

}
