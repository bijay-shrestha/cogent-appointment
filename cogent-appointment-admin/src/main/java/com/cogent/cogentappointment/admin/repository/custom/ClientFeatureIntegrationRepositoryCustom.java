package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("clientFeatureIntegrationRepositoryCustom")
public interface ClientFeatureIntegrationRepositoryCustom {

    Long findByHospitalWiseFeatureIdAndRequestMethod(Long hospitalId, Long featureTypeId, Long requestMethodId);
}
