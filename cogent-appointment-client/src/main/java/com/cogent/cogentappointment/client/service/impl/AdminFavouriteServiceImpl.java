package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.favourite.AdminFavouriteSaveRequestDTO;
import com.cogent.cogentappointment.client.dto.response.favourite.FavouriteDropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.repository.AdminFavouriteRepository;
import com.cogent.cogentappointment.client.repository.AdminRepository;
import com.cogent.cogentappointment.client.repository.FavouriteRepository;
import com.cogent.cogentappointment.client.service.AdminFavouriteService;
import com.cogent.cogentappointment.persistence.model.Admin;
import com.cogent.cogentappointment.persistence.model.Favourite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND_BY_ID;
import static com.cogent.cogentappointment.client.log.constants.AdminLog.*;
import static com.cogent.cogentappointment.client.utils.AdminUtils.parseToSaveFavourtie;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author rupak ON 2020/06/16-12:35 PM
 */
@Service
@Transactional
@Slf4j
public class AdminFavouriteServiceImpl implements AdminFavouriteService {

    private final AdminRepository adminRepository;

    private final AdminFavouriteRepository adminFavouriteRepository;

    private final FavouriteRepository favouriteRepository;

    public AdminFavouriteServiceImpl(AdminRepository adminRepository,
                                     AdminFavouriteRepository adminFavouriteRepository,
                                     FavouriteRepository favouriteRepository) {
        this.adminRepository = adminRepository;
        this.adminFavouriteRepository = adminFavouriteRepository;
        this.favouriteRepository = favouriteRepository;
    }

    @Override
    public List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_ADMIN_FAVOURITE_PROCESS_STARTED, ADMIN_FAVOURITE);

        List<FavouriteDropDownResponseDTO> responseDTOList = adminFavouriteRepository.fetchAdminFavouriteForDropDown();

        log.info(FETCHING_ADMIN_FAVOURITE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }

    @Override
    public List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDownWithIcon() {
        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(FETCHING_ADMIN_FAVOURITE_PROCESS_STARTED, ADMIN_FAVOURITE_WITH_ICON);

        List<FavouriteDropDownResponseDTO> responseDTOList =
                adminFavouriteRepository.fetchAdminFavouriteForDropDownWithIcon();

        log.info(FETCHING_ADMIN_FAVOURITE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return responseDTOList;
    }

    @Override
    public void save(AdminFavouriteSaveRequestDTO saveRequestDTO) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_ADMIN_FAVOURITE_PROCESS_STARTED, ADMIN_FAVOURITE_WITH_ICON);

        Admin admin = adminRepository.findAdminById(saveRequestDTO.getAdminId())
                .orElseThrow(() -> ADMIN_WITH_GIVEN_ID_NOT_FOUND.apply(saveRequestDTO.getAdminId()));

        Favourite favourite = favouriteRepository.findActiveFavouriteById(saveRequestDTO.getFavouriteId())
                .orElseThrow(() -> FAVOURITE_WITH_GIVEN_ID_NOT_FOUND.apply(saveRequestDTO.getFavouriteId()));

        adminFavouriteRepository.save(parseToSaveFavourtie(favourite, admin));

        log.info(SAVING_ADMIN_FAVOURITE_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

    }

    private Function<Long, NoContentFoundException> ADMIN_WITH_GIVEN_ID_NOT_FOUND = (adminId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, ADMIN, adminId);
        throw new NoContentFoundException(Admin.class, "adminId", adminId.toString());
    };

    private Function<Long, NoContentFoundException> FAVOURITE_WITH_GIVEN_ID_NOT_FOUND = (favouriteId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, FAVOURITE, favouriteId);
        throw new NoContentFoundException(Favourite.class, "favouriteId", favouriteId.toString());
    };
}
