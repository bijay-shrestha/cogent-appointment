package com.cogent.cogentappointment.commons.utils;

import com.cogent.cogentappointment.commons.dto.commons.DropDownResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Sauravi Thapa ON 6/15/20
 */
public class AddressUtils {

    public static Function<List<Object[]> ,List<DropDownResponseDTO>> parseToZoneDropDown=results -> {
        List<DropDownResponseDTO> responseDTOS=new ArrayList<>();

        results.forEach(result->{
            final int ID_INDEX = 0;
            final int ZONE_NAME_INDEX = 1;

            DropDownResponseDTO dropDownResponseDTO= DropDownResponseDTO.builder()
                    .value(Long.parseLong(result[ID_INDEX].toString()))
                    .label(result[ZONE_NAME_INDEX].toString())
                    .build();

            responseDTOS.add(dropDownResponseDTO);
        });

        return responseDTOS;
    };
}
