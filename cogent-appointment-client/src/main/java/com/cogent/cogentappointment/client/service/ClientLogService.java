package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;

/**
 * @author Rupak
 */
public interface ClientLogService {

    void save(ClientLogRequestDTO adminRequestDTO, Character status);

}
