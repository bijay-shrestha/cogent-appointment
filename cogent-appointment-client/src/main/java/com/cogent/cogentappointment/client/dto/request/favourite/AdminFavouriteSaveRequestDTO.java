package com.cogent.cogentappointment.client.dto.request.favourite;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/26-1:39 PM
 */
@Getter
@Setter
public class AdminFavouriteSaveRequestDTO implements Serializable {

    @NotNull
    private Long  adminId;

    @NotNull
    private Long  userMenuId;

}
