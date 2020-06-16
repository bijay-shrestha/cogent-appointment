package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AddressRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 6/16/20
 */
@Repository
public interface AddressRepository extends JpaRepository<Address,Long>,AddressRepositoryCustom {
}
