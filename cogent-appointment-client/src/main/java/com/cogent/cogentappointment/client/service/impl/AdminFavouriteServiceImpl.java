package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.client.service.AdminFavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rupak ON 2020/06/16-12:35 PM
 */
@Service
@Transactional
@Slf4j
public class AdminFavouriteServiceImpl implements AdminFavouriteService {
    @Override
    public List<DropDownResponseDTO> fetchAdminFavouriteForDropDown() {
        return null;
    }
}
