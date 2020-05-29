package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.billingMode.BillingModeUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.BillingMode;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author Sauravi Thapa ON 4/17/20
 */
public class BillingModeUtils {


    public static Function<BillingModeRequestDTO, BillingMode> parseToBillingMode = requestDTO -> {
        BillingMode billingMode = new BillingMode();
        billingMode.setName(requestDTO.getName());
        billingMode.setCode(requestDTO.getCode());
        billingMode.setStatus(requestDTO.getStatus());
        billingMode.setDescription(requestDTO.getDescription());
        return billingMode;
    };

    public static BiFunction<BillingModeUpdateRequestDTO, BillingMode,BillingMode> parseToUpdateBillingMode =
            (requestDTO,billingMode) -> {
        billingMode.setName(convertToNormalCase(requestDTO.getName()));
        billingMode.setCode(requestDTO.getCode());
        billingMode.setStatus(requestDTO.getStatus());
        billingMode.setRemarks(requestDTO.getRemarks());
        billingMode.setDescription(requestDTO.getDescription());
        return billingMode;
    };

    public static BiFunction<DeleteRequestDTO, BillingMode,BillingMode> parseToDeletedBillingMode =
            (requestDTO,billingMode) -> {
                billingMode.setStatus(requestDTO.getStatus());
                billingMode.setRemarks(requestDTO.getRemarks());
                return billingMode;
            };
}
