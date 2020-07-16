package com.cogent.cogentthirdpartyconnector.integration.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/10-3:55 PM
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequestDTO implements Serializable {

    private Long transaction_id;

}
