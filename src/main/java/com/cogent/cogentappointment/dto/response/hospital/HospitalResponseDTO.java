package com.cogent.cogentappointment.dto.response.hospital;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Character status;

    private Date createdDate;

    private Date lastModifiedDate;

    private Long createdById;

    private Long modifiedById;

    private String remarks;

    private int totalItems;
}
