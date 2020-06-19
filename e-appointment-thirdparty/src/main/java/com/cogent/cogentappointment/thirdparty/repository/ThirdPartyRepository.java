package com.cogent.cogentappointment.thirdparty.repository;


import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdPartyInfo, Long> {

}
