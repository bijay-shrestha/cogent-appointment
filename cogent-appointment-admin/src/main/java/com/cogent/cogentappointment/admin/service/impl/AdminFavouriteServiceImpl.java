package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.favourite.FavouriteDropDownResponseDTO;
import com.cogent.cogentappointment.admin.repository.AdminFavouriteRepository;
import com.cogent.cogentappointment.admin.service.AdminFavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.admin.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.ADMIN_FAVOURITE;
import static com.cogent.cogentappointment.admin.log.constants.AdminLog.SAVING_PASSWORD_PROCESS_COMPLETED;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/06/16-12:17 PM
 */
@Service
@Transactional
@Slf4j
public class AdminFavouriteServiceImpl implements AdminFavouriteService{

    private final AdminFavouriteRepository adminFavouriteRepository;

    public AdminFavouriteServiceImpl(AdminFavouriteRepository adminFavouriteRepository) {
        this.adminFavouriteRepository = adminFavouriteRepository;
    }


    @Override
    public List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown() {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_FAVOURITE);

        List<FavouriteDropDownResponseDTO> responseDTOList= adminFavouriteRepository.fetchAdminFavouriteForDropDown();

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }
}
