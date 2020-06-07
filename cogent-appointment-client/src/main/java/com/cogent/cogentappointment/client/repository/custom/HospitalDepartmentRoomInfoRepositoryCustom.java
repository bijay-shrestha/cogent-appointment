package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentRoomInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/20/20
 */
@Repository
@Qualifier("hospitalDepartmentRoomInfoRepositoryCustom")
public interface HospitalDepartmentRoomInfoRepositoryCustom {

    List<Object[]> validateRoomDuplicity(Long roomId, Long hospitalId);

    HospitalDepartmentRoomInfo fetchHospitalDepartmentRoomInfo(Long hospitalDepartmentRoomInfoId,
                                                               Long hospitalDepartmentId);
}
