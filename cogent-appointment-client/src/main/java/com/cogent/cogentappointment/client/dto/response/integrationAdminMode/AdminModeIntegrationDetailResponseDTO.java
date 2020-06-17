package com.cogent.cogentappointment.client.dto.response.integrationAdminMode;

import com.cogent.cogentappointment.client.dto.response.integration.ApiQueryParametersDetailResponse;
import com.cogent.cogentappointment.client.dto.response.integration.ApiRequestHeaderDetailResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author rupak on 2020-05-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminModeIntegrationDetailResponseDTO implements Serializable {

    private String appointmentMode;

    private Long featureId;

    private String featureName;

    private Long requestMethodId;

    private String requestMethodName;

    private String integrationChannel;

    private String integrationType;

    private String url;

    private List<ApiRequestHeaderDetailResponse> headers;

    private List<ApiQueryParametersDetailResponse> queryParameters;

    private String createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY 'at' hh:mm a", timezone = "Asia/Kathmandu")
    private Date createdDate;

    private String lastModifiedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, YYYY 'at' hh:mm a", timezone = "Asia/Kathmandu")
    private Date lastModifiedDate;

}
