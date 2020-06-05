package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDepartmentRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
public interface HospitalDepartmentRoomInfoRepository extends JpaRepository<HospitalDepartmentRoomInfo,Long>,
        HospitalDepartmentRoomInfoRepositoryCustom {

    @Query(value = "SELECT hdi FROM HospitalDepartmentRoomInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId" +
            " AND hdi.status!='D'")
    List<HospitalDepartmentRoomInfo> fetchRoomListByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId);

    @Query(value = "SELECT hdi FROM HospitalDepartmentRoomInfo hdi WHERE hdi.hospitalDepartment.id=:hospitalDepartmentId" +
            " AND hdi.room.id=:roomId AND hdi.status!='D'")
    HospitalDepartmentRoomInfo fetchRoomByHospitalDepartmentId(@Param("hospitalDepartmentId") Long hospitalDepartmentId,
                                                               @Param("roomId") Long roomId);


}
