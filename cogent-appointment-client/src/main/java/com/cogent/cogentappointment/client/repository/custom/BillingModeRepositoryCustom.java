package com.cogent.cogentappointment.client.repository.custom;

import com.cogent.cogentappointment.persistence.model.BillingMode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 5/29/20
 */

@Repository
@Qualifier("billingModeRepositoryCustom")
public interface BillingModeRepositoryCustom {
    BillingMode fetchBillingModeByHospitalId(Long hospitalId, Long billingModeId);
}
