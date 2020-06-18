package com.cogent.cogentappointment.client.repository.custom.impl;

import com.cogent.cogentappointment.client.dto.response.favourite.FavoriteDropDownWithIconResponseDTO;
import com.cogent.cogentappointment.client.dto.response.favourite.FavouriteDropDownResponseDTO;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.log.constants.AdminLog;
import com.cogent.cogentappointment.client.repository.custom.AdminFavouriteRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminFavourite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.function.Supplier;

import static com.cogent.cogentappointment.client.log.CommonLogConstant.CONTENT_NOT_FOUND;
import static com.cogent.cogentappointment.client.query.AdminFavouriteQuery.QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN;
import static com.cogent.cogentappointment.client.query.AdminFavouriteQuery.QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN_WITH_ICON;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.createQuery;
import static com.cogent.cogentappointment.client.utils.commons.QueryUtils.transformQueryToResultList;

/**
 * @author rupak ON 2020/06/16-12:22 PM
 */
@Repository
@Transactional(readOnly = true)
@Slf4j
public class AdminFavouriteRepositoryCustomImpl implements AdminFavouriteRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FavouriteDropDownResponseDTO> fetchAdminFavouriteForDropDown() {

        Query query = createQuery.apply(entityManager, QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN);

        List<FavouriteDropDownResponseDTO> list = transformQueryToResultList(query, FavouriteDropDownResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw NO_ADMIN_FAVOURITE_FOUND.get();
        } else return list;
    }


    @Override
    public List<FavoriteDropDownWithIconResponseDTO> fetchAdminFavouriteForDropDownWithIcon() {

        Query query = createQuery.apply(entityManager,
                QUERY_TO_FETCH_ACTIVE_FAVOURITE_FOR_DROPDOWN_WITH_ICON);

        List<FavoriteDropDownWithIconResponseDTO> list =
                transformQueryToResultList(query, FavoriteDropDownWithIconResponseDTO.class);

        if (list.isEmpty()) {
            error();
            throw NO_ADMIN_FAVOURITE_FOUND.get();
        } else return list;
    }

    private Supplier<NoContentFoundException> NO_ADMIN_FAVOURITE_FOUND = () -> new NoContentFoundException(AdminFavourite.class);


    private void error() {
        log.error(CONTENT_NOT_FOUND, AdminLog.ADMIN_FAVOURITE);
    }

}
