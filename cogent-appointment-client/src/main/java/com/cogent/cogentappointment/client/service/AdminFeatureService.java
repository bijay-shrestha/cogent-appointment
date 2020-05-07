package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.adminFeature.AdminFeatureRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;

/**
 * @author smriti on 18/04/20
 */
public interface AdminFeatureService {

    void save(Admin admin);

    void update(AdminFeatureRequestDTO requestDTO);
}

