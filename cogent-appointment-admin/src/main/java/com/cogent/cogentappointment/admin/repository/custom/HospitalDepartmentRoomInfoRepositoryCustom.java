package com.cogent.cogentappointment.admin.repository.custom;

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

}
