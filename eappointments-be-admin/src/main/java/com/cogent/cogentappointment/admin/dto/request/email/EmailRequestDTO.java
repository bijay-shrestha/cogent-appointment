package com.cogent.cogentappointment.admin.dto.request.email;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-08-19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO implements Serializable {

    private String subject;

    private String receiverEmailAddress;

    private String templateName;

    private String paramValue;
}
