package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileStatus;
import com.company.service.CardService;
import com.company.service.ProfileCardService;
import lombok.Setter;

@Setter
public class ProfileCardController {

    private ProfileCardService profileCardService;
    private CardService cardService;

    public void showMenu(ProfileDTO profileDTO) {
        while (true) {
            ProfileStatus status = profileDTO.getStatus();
            int action;
            switch (status) {
                case ACTIVE -> {
                    menu();
                    System.out.print("Enter your action -> ");
                    action = ComponentContainer.getAction();
                    switch (action) {
                        case 1 -> {
                            System.out.println("---- ADD CARD ----");
                            addCardToProfile(profileDTO);
                        }
                        case 2 -> {
                            System.out.println("---- CARD LIST ----");
                            profileCardService.showProfileCardList(profileDTO);
                        }
                        case 3 -> {
                            System.out.println("---- CHANGE CARD STATUS ----");
                            changeCardStatus(profileDTO);
                        }
                        case 4 -> {
                            System.out.println("---- DELETE CARD ----");
                            deleteProfileCard(profileDTO);
                        }
                        case 0 -> {
                            return;
                        }
                        default -> System.out.println("\nError [-_-] try again!\n");
                    }
                }
                case BLOCK -> {
                    System.out.println("You are blocked, Uxladin!");
                    return;
                }
            }
        }
    }

    public void changeCardStatus(ProfileDTO profileDTO) {
        profileCardService.showProfileCardList(profileDTO);
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        CardStatus status = cardService.getStatus();
        profileCardService.changeCardStatus(profileDTO,card_number,status);
    }

    public void deleteProfileCard(ProfileDTO profileDTO) {
        profileCardService.showProfileCardList(profileDTO);
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        profileCardService.deleteProfileCard(profileDTO,card_number);
    }

    public void addCardToProfile(ProfileDTO profileDTO) {
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        profileCardService.addCardToProfile(profileDTO,card_number);
    }

    private void menu() {
        System.out.println("---- CARD MENU ----");
        System.out.println("1.Add Card");
        System.out.println("2.Card List");
        System.out.println("3.Change Card Status");
        System.out.println("4.Delete Card");
        System.out.println("0.Back");
    }

}
