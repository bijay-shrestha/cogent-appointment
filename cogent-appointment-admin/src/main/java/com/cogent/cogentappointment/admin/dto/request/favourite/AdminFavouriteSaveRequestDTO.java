package com.cogent.cogentappointment.admin.dto.request.favourite;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/26-1:39 PM
 */
@Getter
@Setter
public class AdminFavouriteSaveRequestDTO implements Serializable {

    private Long  favouriteId;

    private Long adminId;

}
