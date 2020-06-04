package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
@Qualifier("adminModeQueryParametersRepositoryCustom")
public interface AdminModeQueryParametersRepositoryCustom {

    Map<String,String> findAdminModeApiQueryParameters(Long featureId);
}
