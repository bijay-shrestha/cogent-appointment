package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AdminFavouriteRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AdminFavourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFavouriteRepository extends JpaRepository<AdminFavourite,Long>,
        AdminFavouriteRepositoryCustom {
}
