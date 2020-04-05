package com.cogent.cogentappointment.logging.service;

import com.cogent.cogentappointment.logging.dto.request.client.ClientLogSearchRequestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Rupak
 */
public interface ClientLogService {

    List<ClientLogSearchRequestDTO> search(ClientLogSearchRequestDTO searchRequestDTO, Pageable pageable);
}
