package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.EmailToSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-08-26
 */
@Repository
public interface EmailToSendRepository extends JpaRepository<EmailToSend, Long> {
}
