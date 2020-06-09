package com.cogent.cogentappointment.thirdparty.resource;

import com.cogent.cogentappointment.commons.service.YearMonthDayService;
import com.cogent.cogentappointment.persistence.model.ThirdPartyInfo;
import com.cogent.cogentappointment.persistence.model.YearMonthDay;
import com.cogent.cogentappointment.thirdparty.dto.request.CheckInRequestDTO;
import com.cogent.cogentappointment.thirdparty.dto.response.CheckInResponseDTO;
import com.cogent.cogentappointment.thirdparty.service.ThirdPartyService;
import com.cogent.cogentappointment.thirdparty.utils.ObjectMapperUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
@Api("This is Third Party Resource")
@Slf4j
public class ThirdPartyResource {

    @Autowired
    private ModelMapper modelMapper;

    private final YearMonthDayService yearMonthDayService;

    private final ThirdPartyService thirdPartyService;

    public ThirdPartyResource(YearMonthDayService yearMonthDayService,
                              ThirdPartyService thirdPartyService) {
        this.yearMonthDayService = yearMonthDayService;
        this.thirdPartyService = thirdPartyService;
    }

    @GetMapping
    @ApiOperation("This is Third Party Test API")
    public String test(){
        return "Third Party Tester is running.";
    }

    @GetMapping("/common")
    @ApiOperation("Testing Common Service injection")
    public String commonService(){
        YearMonthDay yearMonthDay = yearMonthDayService.findByYear(2076);
        log.info("YEAR MONTH DAY VALUE IS : {}", yearMonthDay.getAshad());
        return "YEAR MONTH DAY VALUE IS : " +yearMonthDay.getAshad ();
    }


    @PostMapping("/post-ticket")
    @ApiOperation("This is Post Ticket API")
    public ResponseEntity<CheckInResponseDTO> postTicket(@RequestBody CheckInRequestDTO checkInRequestDTO){
        if(checkInRequestDTO.patientId == null) {
            checkInRequestDTO.setPatientId(((Integer) ThreadLocalRandom.current().nextInt()).toString ());
        }
        ThirdPartyInfo thirdPartyInfo = ObjectMapperUtils.convertToEntity(modelMapper, checkInRequestDTO);
        ThirdPartyInfo saveThirdPartyInfo = thirdPartyService.saveThirdPartyData(thirdPartyInfo);
        if(saveThirdPartyInfo != null){
            CheckInResponseDTO checkInResponseDTO = CheckInResponseDTO.builder()
                    .statusCode(200)
                    .responseMessage(saveThirdPartyInfo.getName() + " Data Saved Successfully")
                    .responseData (saveThirdPartyInfo.getPatientId())
                    .build();
            return new ResponseEntity<>(checkInResponseDTO, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
