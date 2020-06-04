package com.cogent.cogentappointment.admin.repository.custom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Qualifier("adminModeRequestHeaderRepositoryCustom")
public interface AdminModeRequestHeaderRepositoryCustom {

    Map<String,String> findAdminModeApiRequestHeaders(Long featureId);
}
