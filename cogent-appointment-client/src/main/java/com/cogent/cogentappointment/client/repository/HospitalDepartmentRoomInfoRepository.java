package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentRoomInfoRepository extends JpaRepository<HospitalDepartmentRoomInfo,Long>,
        HospitalDepartmentRoomInfoRepositoryCustom {
}
