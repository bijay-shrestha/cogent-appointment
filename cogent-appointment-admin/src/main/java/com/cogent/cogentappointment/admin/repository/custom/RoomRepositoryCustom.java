package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.request.room.RoomSearchRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.room.RoomMinimalResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Sauravi Thapa ON 5/19/20
 */

@Repository
@Qualifier("roomRepositoryCustom")
public interface RoomRepositoryCustom {

    Long validateDuplicity(String roomNumber, Long hospitalId);

    Long validateDuplicity(Long id, String roomNumber, Long hospitalId);

    List<DropDownResponseDTO> fetchActiveMinRoom(Long hospitalId);

    List<DropDownResponseDTO> fetchMinRoom(Long hospitalId);

    RoomMinimalResponseDTO search(RoomSearchRequestDTO searchRequestDTO,
                                        Pageable pageable);
}
