package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.checkAvailability.DDRExistingAvailabilityRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.ddrShiftWise.manage.DDRSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.checkAvailability.DDRExistingMinDTO;
import com.cogent.cogentappointment.admin.dto.response.ddrShiftWise.manage.DDRMinResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 08/05/20
 */
@Repository
@Qualifier("ddrShiftWiseRepositoryCustom")
public interface DDRShiftWiseRepositoryCustom {

    Long validateDoctorDutyRosterCount(Long doctorId,
                                       Long specializationId,
                                       Date fromDate,
                                       Date toDate);

    /*EXISTING ROSTERS CHECK AVAILABILITY*/
    List<DDRExistingMinDTO> fetchExistingDDR(DDRExistingAvailabilityRequestDTO requestDTO);

    List<DDRMinResponseDTO> search(DDRSearchRequestDTO searchRequestDTO,
                                   Pageable pageable);
}
