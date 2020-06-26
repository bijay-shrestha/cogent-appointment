package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.dto.response.favourite.FavouriteDropDownResponseDTO;

import java.util.List;

public interface AdminFavouriteService {

    List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown();

    List<FavouriteDropDownResponseDTO>  fetchAdminFavouriteForDropDownWithIcon();
}
