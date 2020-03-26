package com.cogent.cogentappointment.admin.utils;

import com.cogent.cogentappointment.admin.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileDTO;
import com.cogent.cogentappointment.admin.dto.request.companyProfile.CompanyProfileUpdateDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileDetailResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.companyProfile.CompanyProfileResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.profile.ProfileMenuResponseDTO;
import com.cogent.cogentappointment.persistence.model.Hospital;
import com.cogent.cogentappointment.persistence.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.StatusConstants.YES;
import static com.cogent.cogentappointment.admin.utils.commons.StringUtil.convertToNormalCase;

/**
 * @author smriti on 7/8/19
 */
public class CompanyProfileUtils {

    public static Profile convertDTOToCompanyProfile(CompanyProfileDTO profileDTO,
                                                     Hospital company) {
        Profile profile = new Profile();
        profile.setName(convertToNormalCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setStatus(profileDTO.getStatus());
        profile.setCompany(company);
        profile.setIsCompanyProfile(YES);
        return profile;
    }

    public static void convertToUpdatedCompanyProfile(CompanyProfileUpdateDTO profileDTO,
                                                      Profile profile,
                                                      Hospital company) {
        profile.setName(convertToNormalCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setCompany(company);
        profile.setStatus(profileDTO.getStatus());
        profile.setRemarks(profileDTO.getRemarks());
    }

    public static CompanyProfileDetailResponseDTO parseToCompanyProfileDetailResponseDTO(
            CompanyProfileResponseDTO companyProfileInfo,
            List<ProfileMenuResponseDTO> profileMenuResponseDTOS) {

        Map<Long, List<ProfileMenuResponseDTO>> groupByParentId = new TreeMap<>(
                profileMenuResponseDTOS.stream()
                        .collect(Collectors.groupingBy(ProfileMenuResponseDTO::getParentId)));

        return CompanyProfileDetailResponseDTO.builder()
                .companyProfileInfo(companyProfileInfo)
                .companyProfileMenuInfo(groupByParentId)
                .build();
    }

    public static void convertCompanyProfileToDeleted(Profile profile,
                                                      DeleteRequestDTO requestDTO) {

        profile.setStatus(requestDTO.getStatus());
        profile.setRemarks(requestDTO.getRemarks());
    }
}
