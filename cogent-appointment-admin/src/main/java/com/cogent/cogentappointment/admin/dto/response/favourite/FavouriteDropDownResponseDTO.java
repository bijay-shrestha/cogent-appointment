package com.cogent.cogentappointment.admin.dto.response.favourite;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/17-11:11 AM
 */
@Getter
@Setter
public class FavouriteDropDownResponseDTO implements Serializable{

    private String value;

    private String label;

}
