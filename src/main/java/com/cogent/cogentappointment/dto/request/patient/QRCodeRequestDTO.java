package com.cogent.cogentappointment.dto.request.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Rupak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRCodeRequestDTO {

    @NotNull
    @NotEmpty
    private String text;

    @NotNull
    @NotEmpty
    private String subDirectory;


}
