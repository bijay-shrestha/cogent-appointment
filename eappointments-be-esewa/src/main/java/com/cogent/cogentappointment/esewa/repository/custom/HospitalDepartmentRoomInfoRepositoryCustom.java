package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 03/06/20
 */
@Repository
@Qualifier("hospitalDepartmentRoomInfoRepositoryCustom")
public interface HospitalDepartmentRoomInfoRepositoryCustom {

    HospitalDepartmentRoomInfo fetchHospitalDepartmentRoomInfo(Long hospitalDepartmentRoomInfoId,
                                                               Long hospitalDepartmentId);
}
