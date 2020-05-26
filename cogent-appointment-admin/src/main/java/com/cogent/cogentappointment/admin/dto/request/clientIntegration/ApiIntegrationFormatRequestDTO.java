package com.cogent.cogentappointment.admin.dto.request.clientIntegration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author rupak on 2020-05-19
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiIntegrationFormatRequestDTO implements Serializable {

    private Long requestMethodId;

    private String apiUrl;

    private String requestBodyAttrribute;

}
