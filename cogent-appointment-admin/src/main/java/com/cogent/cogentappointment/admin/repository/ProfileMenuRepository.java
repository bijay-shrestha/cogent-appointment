package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.ProfileMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 7/2/19
 */
@Repository
public interface ProfileMenuRepository extends JpaRepository<ProfileMenu, Long> {
}
