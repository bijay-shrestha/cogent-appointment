package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.repository.custom.AppointmentModeRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AppointmentModeRepositoryCustomImpl implements AppointmentModeRepositoryCustom {
    @Override
    public Long validateDuplicity(String name) {
        return null;
    }

    @Override
    public Long validateDuplicity(Long id, String name) {
        return null;
    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinUniversity() {
        return null;
    }
}
