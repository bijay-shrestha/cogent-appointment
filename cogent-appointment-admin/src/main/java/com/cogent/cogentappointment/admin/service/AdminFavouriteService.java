package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.response.favourite.FavouriteDropDownResponseDTO;

import java.util.List;

public interface AdminFavouriteService {

    List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown();
}
