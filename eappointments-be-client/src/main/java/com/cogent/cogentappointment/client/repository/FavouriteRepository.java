package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite,Long> {

    @Query("SELECT fav FROM Favourite fav WHERE fav.id=:id AND fav.status='Y'")
    Optional<Favourite> findActiveFavouriteById(@Param("id") Long favouriteId);
}
