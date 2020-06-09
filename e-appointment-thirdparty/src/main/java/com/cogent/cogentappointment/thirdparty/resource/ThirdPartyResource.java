package com.cogent.cogentappointment.thirdparty.resource;

import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import com.cogent.cogentappointment.thirdparty.dto.request.CheckInRequestDTO;
import com.cogent.cogentappointment.thirdparty.dto.response.CheckInResponseDTO;
import com.cogent.cogentappointment.thirdparty.service.ThirdPartyService;
import com.cogent.cogentappointment.thirdparty.utils.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/api/v1/test")
@Api("This is Third Party Resource")
public class ThirdPartyResource {

    @Autowired
    private ModelMapper modelMapper;

    private final ThirdPartyService thirdPartyService;

    public ThirdPartyResource(ThirdPartyService thirdPartyService) {
        this.thirdPartyService = thirdPartyService;
    }

    @GetMapping
    @ApiOperation("This is Third Party Test API")
    public String test() {
        return "Third Party Tester is running.";
    }

    @PostMapping("/post-ticket")
    @ApiOperation("This is Post Ticket API")
    public ResponseEntity<CheckInResponseDTO> postTicket(@RequestBody CheckInRequestDTO checkInRequestDTO) {
        if (checkInRequestDTO.getPatientId() == null) {
            checkInRequestDTO.setPatientId(((Integer) ThreadLocalRandom.current().nextInt()).toString());
        }
        ThirdPartyInfo thirdPartyInfo = ObjectMapperUtils.convertToEntity(modelMapper, checkInRequestDTO);
        ThirdPartyInfo saveThirdPartyInfo = thirdPartyService.saveThirdPartyData(thirdPartyInfo);
        if (saveThirdPartyInfo != null) {
            CheckInResponseDTO checkInResponseDTO = CheckInResponseDTO.builder()
                    .statusCode(200)
//                    .responseMessage(saveThirdPartyInfo.getName() + " Data Saved Successfully")
                    .responseData(saveThirdPartyInfo.getPatientId())
                    .build();
            return new ResponseEntity<>(checkInResponseDTO, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
