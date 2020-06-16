package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;

import java.util.List;

public interface AdminFavouriteService {

    List<DropDownResponseDTO> fetchAdminFavouriteForDropDown();
}
