package com.cogent.cogentappointment.client.loghandler;

import com.cogent.cogentappointment.client.dto.commons.ClientLogRequestDTO;
import com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils;

import java.io.IOException;

import static com.cogent.cogentappointment.client.utils.commons.StringUtil.splitByCharacterTypeCamelCase;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;

/**
 * @author Rupak
 */
public class RequestHandler {

    public static ClientLogRequestDTO convertToClientLogRequestDTO(String userLog) throws IOException {

        ClientLogRequestDTO clientLogRequestDTO = ObjectMapperUtils.map(userLog, ClientLogRequestDTO.class);

        //todo: rupak
//        getLoggedInAdminEmail();
//        clientLogRequestDTO.setAdminId(getLoggedInCompanyId());
        clientLogRequestDTO.setFeature(toNormalCase(splitByCharacterTypeCamelCase(clientLogRequestDTO.getFeature())));

        return clientLogRequestDTO;
    }
}
