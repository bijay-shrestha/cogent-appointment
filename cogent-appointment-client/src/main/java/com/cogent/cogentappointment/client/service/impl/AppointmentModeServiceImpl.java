package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.repository.AppointmentModeRepository;
import com.cogent.cogentappointment.client.service.AppointmentModeService;
import com.cogent.cogentappointment.persistence.model.AppointmentMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sauravi Thapa ON 4/17/20
 */

@Service
@Transactional
@Slf4j
public class AppointmentModeServiceImpl implements AppointmentModeService {

    private final AppointmentModeRepository repository;

    public AppointmentModeServiceImpl(AppointmentModeRepository repository) {
        this.repository = repository;
    }


    @Override
    public void delete(DeleteRequestDTO deleteRequestDTO) {

    }

    @Override
    public List<DropDownResponseDTO> fetchActiveMinUniversity() {
        return null;
    }

    @Override
    public AppointmentMode findActiveUniversityById(Long id) {
        return null;
    }
}
