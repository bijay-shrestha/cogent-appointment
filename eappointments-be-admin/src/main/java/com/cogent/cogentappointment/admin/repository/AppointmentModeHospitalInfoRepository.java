package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentModeHospitalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author rupak on 2020-05-25
 */
@Repository
public interface AppointmentModeHospitalInfoRepository extends JpaRepository<AppointmentModeHospitalInfo,Long> {

    @Query("Select amhi from AppointmentModeHospitalInfo amhi where amhi.hospitalId.id=:companyId AND amhi.status='Y'")
    Optional<List<AppointmentModeHospitalInfo>> findAppointmentModeHospitalInfoByHospitalId(@Param("companyId") Long companyId);
}
