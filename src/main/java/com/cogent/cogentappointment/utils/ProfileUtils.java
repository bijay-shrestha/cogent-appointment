package com.cogent.cogentappointment.utils;

import com.cogent.cogentappointment.dto.commons.DeleteRequestDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileDTO;
import com.cogent.cogentappointment.dto.request.profile.ProfileUpdateDTO;
import com.cogent.cogentappointment.dto.response.profile.*;
import com.cogent.cogentappointment.model.Department;
import com.cogent.cogentappointment.model.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cogent.cogentappointment.constants.StringConstant.COMMA_SEPARATED;
import static com.cogent.cogentappointment.utils.commons.StringUtil.toUpperCase;

/**
 * @author smriti on 7/8/19
 */
public class ProfileUtils {

    public static Profile convertDTOToProfile(ProfileDTO profileDTO,
                                              Department department) {
        Profile profile = new Profile();
        profile.setName(toUpperCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setStatus(profileDTO.getStatus());
        profile.setDepartment(department);
        return profile;
    }

    public static Profile convertToUpdatedProfile(ProfileUpdateDTO profileDTO,
                                                  Department department,
                                                  Profile profile) {
        profile.setName(toUpperCase(profileDTO.getName()));
        profile.setDescription(profileDTO.getDescription());
        profile.setStatus(profileDTO.getStatus());
        profile.setDepartment(department);
        profile.setRemarks(profileDTO.getRemarks());
        return profile;
    }

    public static ProfileDetailResponseDTO parseToProfileDetailResponseDTO(
            ProfileResponseDTO profileResponseDTO,
            List<ProfileMenuResponseDTO> profileMenuResponseDTOS) {

        Map<Long, List<ProfileMenuResponseDTO>> groupByParentId = new TreeMap<>(
                profileMenuResponseDTOS.stream()
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

        return parseToAssignedProfileMenuResponseDTO(assignedRolesResponseDTOS, results);
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

    private static AssignedProfileResponseDTO parseToAssignedProfileMenuResponseDTO
            (List<AssignedRolesResponseDTO> assignedRolesResponseDTOS,
             List<Object[]> queryResults) {

        /*BECAUSE DEPARTMENT DETAILS REMAIN THE SAME*/
        final int DEPARTMENT_CODE_INDEX = 3;
        final int DEPARTMENT_NAME_INDEX = 4;

        Object[] firstObject = queryResults.get(0);
        String departmentName = firstObject[DEPARTMENT_NAME_INDEX].toString();
        String departmentCode = firstObject[DEPARTMENT_CODE_INDEX].toString();

        return AssignedProfileResponseDTO.builder()
                .departmentName(departmentName)
                .departmentCode(departmentCode)
                .assignedRolesResponseDTOS(assignedRolesResponseDTOS)
                .build();
    }
}
