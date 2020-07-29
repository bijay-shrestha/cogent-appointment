package com.cogent.cogentappointment.admin.repository.custom;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
@Repository
@Qualifier("httpRequestMethodRepositoryCustom")
public interface HttpRequestMethodRepositoryCustom {

    List<DropDownResponseDTO> fetchActiveRequestMethod();
}
