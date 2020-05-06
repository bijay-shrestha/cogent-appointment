package com.cogent.cogentappointment.client.utils;

import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuRequestDTO;
import com.cogent.cogentappointment.client.dto.request.profile.ProfileMenuUpdateRequestDTO;
import com.cogent.cogentappointment.persistence.model.Profile;
import com.cogent.cogentappointment.persistence.model.ProfileMenu;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author smriti on 7/10/19
 */
public class ProfileMenuUtils {

    public static List<ProfileMenu> convertToProfileMenu(Profile profile, List<ProfileMenuRequestDTO> requestDTO) {
        List<ProfileMenu> profileMenuList = requestDTO.stream()
                .map(profileMenu -> convertToProfileMenuResponse.apply(profile, profileMenu))
                .collect(Collectors.toList());

        setLoginAndForgotPasswordMenu(profile, profileMenuList);

        return profileMenuList;
    }

    private static void setLoginAndForgotPasswordMenu(Profile profile, List<ProfileMenu> profileMenuList) {

        //login profilemenu
        ProfileMenu loginMenu = new ProfileMenu();
        loginMenu.setProfile(profile);
        loginMenu.setParentId(8080l);
        loginMenu.setRoleId(3001l);
        loginMenu.setUserMenuId(8008l);
        loginMenu.setStatus('Y');

        profileMenuList.add(loginMenu);

        //forgot password profile menu
        ProfileMenu forgotPassword = new ProfileMenu();
        loginMenu.setProfile(profile);
        loginMenu.setParentId(8081l);
        loginMenu.setRoleId(3002l);
        loginMenu.setUserMenuId(8008l);

        loginMenu.setStatus('Y');

        profileMenuList.add(forgotPassword);

    }


    private static BiFunction<Profile, ProfileMenuRequestDTO, ProfileMenu> convertToProfileMenuResponse =
            (profile, profileMenuRequestDTO) -> {
                ProfileMenu profileMenu = new ProfileMenu();
                profileMenu.setProfile(profile);
                profileMenu.setParentId(profileMenuRequestDTO.getParentId());
                profileMenu.setUserMenuId(profileMenuRequestDTO.getUserMenuId());
                profileMenu.setRoleId(profileMenuRequestDTO.getRoleId());
                profileMenu.setStatus(profileMenuRequestDTO.getStatus());
                return profileMenu;
            };

    public static List<ProfileMenu> convertToUpdatedProfileMenu(Profile profile,
                                                                List<ProfileMenuUpdateRequestDTO> requestDTO) {
        return requestDTO.stream()
                .map(profileMenu -> convertToUpdatedProfileMenuResponse.apply(profile, profileMenu))
                .collect(Collectors.toList());
    }

    private static BiFunction<Profile, ProfileMenuUpdateRequestDTO, ProfileMenu> convertToUpdatedProfileMenuResponse =
            (profile, profileMenuRequestDTO) -> {

                ProfileMenu profileMenu = new ProfileMenu();
                profileMenu.setId(profileMenuRequestDTO.getProfileMenuId());
                profileMenu.setProfile(profile);
                profileMenu.setParentId(profileMenuRequestDTO.getParentId());
                profileMenu.setUserMenuId(profileMenuRequestDTO.getUserMenuId());
                profileMenu.setRoleId(profileMenuRequestDTO.getRoleId());
                profileMenu.setStatus(profileMenuRequestDTO.getStatus());
                return profileMenu;
            };
}
