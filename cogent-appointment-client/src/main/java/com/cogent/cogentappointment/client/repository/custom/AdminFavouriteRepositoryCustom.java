package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("adminFavouriteRepositoryCustom")
public interface AdminFavouriteRepositoryCustom {

    List<DropDownResponseDTO> fetchAdminFavouriteForDropDown();
}
