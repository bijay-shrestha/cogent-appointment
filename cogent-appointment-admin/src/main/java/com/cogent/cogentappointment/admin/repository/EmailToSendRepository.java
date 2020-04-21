package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.EmailToSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 2019-08-26
 */
@Repository
public interface EmailToSendRepository extends JpaRepository<EmailToSend, Long> {

    @Query("SELECT e FROM EmailToSend e WHERE e.isSent = 'N'")
    List<EmailToSend> fetchUnsentEmails();
}
