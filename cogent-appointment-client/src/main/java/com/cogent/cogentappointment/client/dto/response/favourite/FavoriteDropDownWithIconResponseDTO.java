package com.cogent.cogentappointment.client.dto.response.favourite;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FavoriteDropDownWithIconResponseDTO implements Serializable {

    private String value;

    private String label;

}