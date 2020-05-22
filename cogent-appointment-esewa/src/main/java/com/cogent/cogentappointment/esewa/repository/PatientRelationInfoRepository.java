package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.PatientRelationInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.PatientRelationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 28/02/20
 */
@Repository
public interface PatientRelationInfoRepository extends JpaRepository<PatientRelationInfo, Long>,
        PatientRelationInfoRepositoryCustom {
}
