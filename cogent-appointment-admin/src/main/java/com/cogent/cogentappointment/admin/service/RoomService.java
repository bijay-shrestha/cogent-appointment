package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.room.RoomRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.room.RoomUpdateRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.room.RoomMinimalResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/19/20
 */
public interface RoomService {
    void save(RoomRequestDTO requestDTO);

    void update(RoomUpdateRequestDTO requestDTO);

    void delete(DeleteRequestDTO deleteRequestDTO);

    List<DropDownResponseDTO> fetchActiveMinRoom(Long hospitalId);

    List<DropDownResponseDTO> fetchMinRoom(Long hospitalId);

    RoomMinimalResponseDTO search(RoomSearchRequestDTO searchRequestDTO,
                                        Pageable pageable);
}
