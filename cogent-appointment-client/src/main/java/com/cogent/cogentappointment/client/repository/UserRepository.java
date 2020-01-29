package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 08/11/2019
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT a FROM User a WHERE a.username=:username AND a.status='Y'")
    User getLoggedInUser(@Param("username") String username);
}
