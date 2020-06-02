package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update.HospitalDeptDutyRosterOverrideUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterOverrideRepositoryCustom")
public interface HospitalDeptDutyRosterOverrideRepositoryCustom {

    Long fetchOverrideCountWithoutRoom(Long hospitalDepartmentId, Date fromDate, Date toDate);

    Long fetchOverrideCountWithRoom(Long hospitalDepartmentId, Date fromDate, Date toDate, Long roomId);

    Long fetchOverrideCountWithoutRoomExceptCurrentId(Long hospitalDepartmentId,
                                                      Date fromDate, Date toDate, Long rosterOverrideId);

    Long fetchOverrideCountWithRoomExceptCurrentId(Long hospitalDepartmentId, Date fromDate,
                                                   Date toDate, Long roomId, Long rosterOverrideId);

    void updateOverrideStatus(Long hddRosterId);

    void updateOverrideRoomInfo(Long hddRosterId, Long roomId);

    List<HospitalDepartmentDutyRosterOverride> fetchOverrideList(
            List<HospitalDeptDutyRosterOverrideUpdateRequestDTO> updateRequestDTOS);

}
