package com.cogent.cogentappointment.client.dto.commons;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientLogRequestDTO implements Serializable {

    @NotNull
    private Long adminId;

    @NotNull
    private Long parentId;

    @NotNull
    private Long roleId;

    @NotNull
    @NotEmpty
    private String logDescription;

    @NotNull
    @NotEmpty
    private String browser;

    @NotNull
    @NotEmpty
    private String operatingSystem;

    @NotNull
    @NotEmpty
    private String ipAddress;

    @NotNull
    @NotEmpty
    private String feature;

    @NotNull
    @NotEmpty
    private String actionType;
}
