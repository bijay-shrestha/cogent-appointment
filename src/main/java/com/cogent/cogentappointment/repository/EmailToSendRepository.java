package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.EmailToSend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-08-26
 */
@Repository
public interface EmailToSendRepository extends JpaRepository<EmailToSend, Long> {
}
