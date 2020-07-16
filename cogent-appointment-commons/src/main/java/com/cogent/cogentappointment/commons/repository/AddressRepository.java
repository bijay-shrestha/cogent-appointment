package com.cogent.cogentappointment.commons.repository;

import com.cogent.cogentappointment.commons.repository.custom.AddressRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, AddressRepositoryCustom {

    @Query("SELECT a FROM Address a WHERE a.id =:id")
    Optional<Address> fetchAddressById(@Param("id") Long id);

}
