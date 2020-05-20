package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.client.dto.request.room.RoomUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.room.RoomMinimalResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public interface RoomService {
    void save(RoomRequestDTO requestDTO);

    void update(RoomUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DropDownResponseDTO> fetchActiveMinRoom();

    List<DropDownResponseDTO> fetchMinRoom();

    RoomMinimalResponseDTO search(RoomSearchRequestDTO searchRequestDTO,
                                        Pageable pageable);
}
