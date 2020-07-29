package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileUpdateDTO;
import com.cogent.cogentappointment.client.dto.response.profile.*;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.client.constants.StatusConstants.NO;
import static com.cogent.cogentappointment.client.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.client.utils.commons.StringUtil.toNormalCase;

/**
 * @author smriti on 7/8/19
 */
public class ProfileUtils {

    public static Profile convertDTOToProfile(ProfileDTO profileDTO,
                                              Department department) {
        Profile profile = new Profile();
        profile.setName(toNormalCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setStatus(profileDTO.getStatus());
        profile.setDepartment(department);
        profile.setIsAllRoleAssigned(profileDTO.getIsAllRoleAssigned());
        profile.setIsSuperAdminProfile(NO);
        profile.setIsCompanyProfile(NO);
        return profile;
    }

    public static void convertToUpdatedProfile(ProfileUpdateDTO profileDTO,
                                               Department department,
                                               Profile profile) {

        profile.setName(toNormalCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setIsAllRoleAssigned(profileDTO.getIsAllRoleAssigned());
        profile.setStatus(profileDTO.getStatus());
        profile.setDepartment(department);
        profile.setRemarks(profileDTO.getRemarks());
    }

    public static ProfileDetailResponseDTO parseToProfileDetailResponseDTO(
            ProfileResponseDTO profileResponseDTO,
            List<ProfileMenuResponseDTO> profileMenuResponseDTOS) {

        Map<Long, List<ProfileMenuResponseDTO>> groupByParentId =
                new TreeMap<>(profileMenuResponseDTOS.stream()
                        .collect(Collectors.groupingBy(ProfileMenuResponseDTO::getParentId)));

        return ProfileDetailResponseDTO.builder()
                .profileResponseDTO(profileResponseDTO)
                .profileMenuResponseDTOS(groupByParentId)
                .build();
    }

    public static BiFunction<Profile, DeleteRequestDTO, Profile> convertProfileToDeleted =
            (profile, deleteRequestDTO) -> {
                profile.setStatus(deleteRequestDTO.getStatus());
                profile.setRemarks(deleteRequestDTO.getRemarks());
                return profile;
            };

    public static AssignedProfileResponseDTO parseToAssignedProfileMenuResponseDTO(List<Object[]> results) {

        List<ChildMenusResponseDTO> childMenusResponseDTOS = parseToChildMenusResponseDTOS(results);

        List<AssignedRolesResponseDTO> assignedRolesResponseDTOS =
                parseToAssignedRolesResponseDTOS(childMenusResponseDTOS);

        return AssignedProfileResponseDTO.builder()
                .assignedRolesResponseDTOS(assignedRolesResponseDTOS)
                .build();
    }

    private static List<ChildMenusResponseDTO> parseToChildMenusResponseDTOS(List<Object[]> results) {

        final int PARENT_ID_INDEX = 0;
        final int USER_MENU_ID_INDEX = 1;
        final int ROLE_ID_INDEX = 2;

        List<ChildMenusResponseDTO> childMenusResponseDTOS = new ArrayList<>();

        results.forEach(result -> {
            List<Long> roleIds = Stream.of(result[ROLE_ID_INDEX].toString()
                    .split(COMMA_SEPARATED))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            ChildMenusResponseDTO responseDTO = ChildMenusResponseDTO.builder()
                    .parentId(Long.parseLong(result[PARENT_ID_INDEX].toString()))
                    .userMenuId(Long.parseLong(result[USER_MENU_ID_INDEX].toString()))
                    .roleId(roleIds)
                    .build();

            childMenusResponseDTOS.add(responseDTO);
        });

        return childMenusResponseDTOS;
    }

    private static List<AssignedRolesResponseDTO> parseToAssignedRolesResponseDTOS
            (List<ChildMenusResponseDTO> childMenusResponseDTOS) {

        Map<Long, List<ChildMenusResponseDTO>> groupByParentIdMap =
                childMenusResponseDTOS.stream().collect(Collectors.groupingBy(ChildMenusResponseDTO::getParentId));

        return groupByParentIdMap.entrySet()
                .stream()
                .map(map -> new AssignedRolesResponseDTO(map.getKey(), map.getValue()))
                .collect(Collectors.toList());
    }
}
