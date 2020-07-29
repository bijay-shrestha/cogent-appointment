package com.cogent.cogentappointment.client.dto.request.profile;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 27/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileMenuSearchRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String email;
}
