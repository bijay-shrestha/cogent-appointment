package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DoctorSalutationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.DoctorSalutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rupak on 2020-05-13
 */
@Repository
public interface DoctorSalutationRepository extends JpaRepository<DoctorSalutation,Long>, DoctorSalutationRepositoryCustom {

}
