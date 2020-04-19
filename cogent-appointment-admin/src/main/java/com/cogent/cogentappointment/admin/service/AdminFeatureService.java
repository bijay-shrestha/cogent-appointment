package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.adminFeature.AdminFeatureRequestDTO;
import com.cogent.cogentappointment.persistence.model.Admin;

/**
 * @author smriti on 18/04/20
 */
public interface AdminFeatureService {

    void save(Admin admin, Character isSideBarCollapse);

    void update(AdminFeatureRequestDTO requestDTO);

    void update(Long adminId, Character isSideBarCollapse);
}

