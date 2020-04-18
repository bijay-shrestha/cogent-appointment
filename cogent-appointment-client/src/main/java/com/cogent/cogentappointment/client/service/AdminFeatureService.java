package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.persistence.model.Admin;

/**
 * @author smriti on 18/04/20
 */
public interface AdminFeatureService {

    void save(Admin admin, Character isSideBarCollapse);

    void update(Long adminId, Character isSideBarCollapse);
}

