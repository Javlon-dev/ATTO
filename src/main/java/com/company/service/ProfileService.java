package com.company.service;

import com.company.container.ComponentContainer;
import com.company.controller.ProfileController;
import com.company.dto.CardDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.repository.ProfileRepository;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Setter
public class ProfileService {

    private ProfileRepository profileRepository;
    private ProfileController profileController;

    public boolean checkPhone() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            code.append((int) (Math.random() * 10));
        }
        System.out.println("Your code: " + code);
        System.out.print("Enter code -> ");
        String pCode = ComponentContainer.getLine();
        return code.toString().equals(pCode);
    }

    public void registrationProfile(String name, String surname, String phone, String password) {
        if (name.isEmpty() || surname.isEmpty() || password.isEmpty()){
            System.out.println("Error information!, Mazgi");
            return;
        }
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(name);
        profileDTO.setSurname(surname);
        profileDTO.setPhone(phone);
        profileDTO.setPassword(password);
        profileDTO.setCreated_date(LocalDateTime.now());
        profileDTO.setStatus(ProfileStatus.ACTIVE);
        profileDTO.setRole(ProfileRole.USER);
        ProfileDTO dto = profileRepository.registrationProfile(profileDTO);
        if (dto != null) {
            System.out.println("Your Profile Id: " + dto.getId());
        } else {
            System.out.println("Mazgi, try again!");
        }
    }

    public void loginProfile(String phone, String password) {
        ProfileDTO profileDTO = profileRepository.loginProfile(phone, password);
        if (profileDTO == null) {
            System.out.println("Mazgi, Wrong phone or password! Try again");
            System.out.println("Your action is phone: " + phone + " password: " + password);
            return;
        }
        if (profileDTO.getStatus().equals(ProfileStatus.BLOCK)){
            System.out.println("User is blocked! please Mazgi qilmen");
            return;
        }
        profileController.showMenu(profileDTO);
    }

    public void changeProfileStatus(String phone, ProfileStatus status) {
        if (getProfileByPhone(phone) == null){
            return;
        }
        ProfileDTO profileDTO = profileRepository.changeProfileStatus(phone, status);
        Optional<ProfileDTO> optional = Optional.ofNullable(profileDTO);
        if (optional.isPresent()) {
            System.out.println(profileDTO);
            System.out.println("Successfully changed!");
        }else System.out.println("Mazgi nitoku! try again");
    }

    public ProfileDTO getProfileByPhone(String phone) {
        ProfileDTO profileDTO = profileRepository.getProfileByPhone(phone);
        Optional<ProfileDTO> optional = Optional.ofNullable(profileDTO);
        if (optional.isEmpty()) {
            System.out.println("Wrong card number! Mazgi");
            return null;
        }
        return profileDTO;
    }

    public void showProfileList() {
        List<ProfileDTO> dtoList = profileRepository.getProfileList();
        for (ProfileDTO dto : dtoList) {
            System.out.println(dto);
        }
    }

    public ProfileStatus getStatus() {
        int action;
        while (true) {
            statusMenu();
            System.out.print("Enter your action -> ");
            action = ComponentContainer.getAction();
            switch (action) {
                case 1 -> {
                    return ProfileStatus.ACTIVE;
                }
                case 2 -> {
                    return ProfileStatus.BLOCK;
                }
                default -> System.out.println("\nError [-_-] try again!\n");
            }
        }
    }

    public void statusMenu() {
        System.out.println("Choose status");
        System.out.println("1.Active");
        System.out.println("2.Block");
    }
}
