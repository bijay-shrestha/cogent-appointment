package com.cogent.cogentthirdpartyconnector.integration.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/10-3:54 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointCheckInRequestDTO implements Serializable{

    private Long hospitalNumber;

}
