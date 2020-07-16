package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.favourite.AdminFavouriteSaveRequestDTO;
import com.cogent.cogentappointment.client.dto.request.favourite.AdminFavouriteUpdateRequestDTO;
import com.cogent.cogentappointment.client.dto.response.favourite.FavouriteDropDownResponseDTO;

import java.util.List;

public interface AdminFavouriteService {

    List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown();

    List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDownWithIcon();

    void save(AdminFavouriteSaveRequestDTO adminFavouriteSaveRequestDTO);

    void update(AdminFavouriteUpdateRequestDTO requestDTO);

    List<Long> getAdminFavouriteByAdminId(Long adminId);
}
