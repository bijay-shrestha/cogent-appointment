package com.cogent.cogentappointment.admin.dto.request.favourite;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/26-2:18 PM
 */
@Getter
@Setter
public class AdminFavouriteUpdateRequestDTO implements Serializable{

    @NotNull
    private Long adminId;

    @NotNull
    private Long userMenuId;

    @NotNull
    private Character status;


}
