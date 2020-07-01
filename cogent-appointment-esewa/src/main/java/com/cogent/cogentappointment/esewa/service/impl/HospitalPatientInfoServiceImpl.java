package com.cogent.cogentappointment.esewa.service.impl;

import com.cogent.cogentappointment.commons.repository.AddressRepository;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestByDTO;
import com.cogent.cogentappointment.esewa.dto.request.patient.PatientRequestForDTO;
import com.cogent.cogentappointment.esewa.exception.BadRequestException;
import com.cogent.cogentappointment.esewa.exception.NoContentFoundException;
import com.cogent.cogentappointment.esewa.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.esewa.service.HospitalPatientInfoService;
import com.cogent.cogentappointment.persistence.model.Address;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentappointment.persistence.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.function.Function;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.AppointmentServiceMessage.INVALID_ADDRESS_INFO;
import static com.cogent.cogentappointment.esewa.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.esewa.log.CommonLogConstant.*;
import static com.cogent.cogentappointment.esewa.log.constants.PatientLog.HOSPITAL_PATIENT_INFO;
import static com.cogent.cogentappointment.esewa.utils.HospitalPatientInfoUtils.*;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.esewa.utils.commons.DateUtils.getTimeInMillisecondsFromLocalDate;

/**
 * @author smriti on 29/02/20
 */
@Service
@Transactional
@Slf4j
public class HospitalPatientInfoServiceImpl implements HospitalPatientInfoService {

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    private final AddressRepository addressRepository;

    public HospitalPatientInfoServiceImpl(HospitalPatientInfoRepository hospitalPatientInfoRepository,
                                          AddressRepository addressRepository) {
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void saveHospitalPatientInfoForSelf(Hospital hospital, Patient patient,
                                               PatientRequestByDTO patientRequestByDTO,
                                               Character hasAddress) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_PATIENT_INFO);

        Long hospitalPatientInfoCount = fetchHospitalPatientInfoCount(patient.getId(), hospital.getId());

        if (hospitalPatientInfoCount.intValue() <= 0)
            saveHospitalPatientInfo(hospital, patient,
                    patientRequestByDTO.getEmail(),
                    patientRequestByDTO.getAddress(),
                    hasAddress,
                    patientRequestByDTO.getProvinceId(),
                    patientRequestByDTO.getVdcOrMunicipalityId(),
                    patientRequestByDTO.getDistrictId(),
                    patientRequestByDTO.getWardNumber()
            );

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_PATIENT_INFO, getDifferenceBetweenTwoTime(startTime));
    }

    @Override
    public void saveHospitalPatientInfoForOthers(Hospital hospital, Patient patient,
                                                 PatientRequestForDTO patientRequestForDTO,
                                                 Character hasAddress) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(SAVING_PROCESS_STARTED, HOSPITAL_PATIENT_INFO);

        HospitalPatientInfo hospitalPatientInfo = fetchHospitalPatientInfo(patient.getId(), hospital.getId());

        if (Objects.isNull(hospitalPatientInfo))
            saveHospitalPatientInfo(
                    hospital, patient,
                    patientRequestForDTO.getEmail(),
                    patientRequestForDTO.getAddress(),
                    hasAddress,
                    patientRequestForDTO.getProvinceId(),
                    patientRequestForDTO.getVdcOrMunicipalityId(),
                    patientRequestForDTO.getDistrictId(),
                    patientRequestForDTO.getWardNumber());

        else
            updateHospitalPatientInfo(
                    patientRequestForDTO.getEmail(),
                    patientRequestForDTO.getAddress(), hospitalPatientInfo
            );

        log.info(SAVING_PROCESS_COMPLETED, HOSPITAL_PATIENT_INFO, getDifferenceBetweenTwoTime(startTime));
    }

    private void saveHospitalPatientInfo(Hospital hospital, Patient patient,
                                         String email, String address,
                                         Character hasAddress,
                                         Long provinceId,
                                         Long vdcOrMunicipalityId,
                                         Long districtId,
                                         String wardNumber) {

        HospitalPatientInfo hospitalPatientInfo = parseHospitalPatientInfo
                (hospital, patient, email, address, hasAddress);

        if (hasAddress.equals(YES)) {
            validateAddressInfo(provinceId, districtId, vdcOrMunicipalityId, wardNumber);

            Address province = fetchAddress(provinceId);
            Address vdcOrMunicipality = fetchAddress(vdcOrMunicipalityId);
            Address district = fetchAddress(districtId);

            parsePatientAddressDetails(province, vdcOrMunicipality, district, wardNumber, hospitalPatientInfo);
        }

        hospitalPatientInfoRepository.save(hospitalPatientInfo);
    }

    private Long fetchHospitalPatientInfoCount(Long patientId, Long hospitalId) {
        return hospitalPatientInfoRepository.fetchHospitalPatientInfoCount(patientId, hospitalId);
    }

    private HospitalPatientInfo fetchHospitalPatientInfo(Long patientId, Long hospitalId) {
        return hospitalPatientInfoRepository.fetchHospitalPatientInfo(patientId, hospitalId);
    }

    private Address fetchAddress(Long addressId) {
        return addressRepository.fetchAddressById(addressId)
                .orElseThrow(() -> ADDRESS_WITH_GIVEN_ID_NOT_FOUND.apply(addressId));
    }

    private Function<Long, NoContentFoundException> ADDRESS_WITH_GIVEN_ID_NOT_FOUND = (addressId) -> {
        log.error(CONTENT_NOT_FOUND_BY_ID, "Address");
        throw new NoContentFoundException(Address.class, "addressId", addressId.toString());
    };


    private void validateAddressInfo(Long provinceId,
                                     Long districtId,
                                     Long vdcOrMunicipalityId,
                                     String wardNumber) {

        if (Objects.isNull(provinceId) || Objects.isNull(districtId)
                || Objects.isNull(vdcOrMunicipalityId) || ObjectUtils.isEmpty(wardNumber))
            throw new BadRequestException(INVALID_ADDRESS_INFO);
    }
}
