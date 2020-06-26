package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.response.favourite.FavouriteDropDownResponseDTO;
import com.cogent.cogentappointment.client.repository.AdminFavouriteRepository;
import com.cogent.cogentappointment.client.service.AdminFavouriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.FETCHING_PROCESS_STARTED;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/06/16-12:35 PM
 */
@Service
@Transactional
@Slf4j
public class AdminFavouriteServiceImpl implements AdminFavouriteService {

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

    @Override
    public List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDownWithIcon() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_PROCESS_STARTED, ADMIN_FAVOURITE_WITH_ICON);

        List<FavouriteDropDownResponseDTO> responseDTOList =
                adminFavouriteRepository.fetchAdminFavouriteForDropDownWithIcon();

        log.info(SAVING_PASSWORD_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }
}
