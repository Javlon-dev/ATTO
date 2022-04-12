package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.service.CardService;
import com.company.service.ProfileCardService;
import com.company.service.ProfileService;
import com.company.service.TransactionService;
import lombok.Setter;

@Setter
public class ProfileController {

    private ProfileService profileService;
    private CardController cardController;
    private CardService cardService;
    private ProfileCardController profileCardController;
    private ProfileCardService profileCardService;
    private TerminalController terminalController;
    private TransactionController transactionController;
    private TransactionService transactionService;

    public void registrationProfile() {
        System.out.print("Enter your name -> ");
        String name = ComponentContainer.getLine();
        System.out.print("Enter your surname -> ");
        String surname = ComponentContainer.getLine();
        StringBuilder phone = new StringBuilder("+998");
        System.out.print("Enter your phone -> " + phone);
        phone.append(ComponentContainer.getLine());
//        if (!ComponentContainer.PROFILE_SERVICE.checkPhone()) {
//            System.out.println("Wrong code! Try again");
//            return;
//        }
        System.out.print("Create your password -> ");
        String password = ComponentContainer.getLine();
        profileService.registrationProfile(name.trim(), surname.trim(),
                phone.toString().trim(), password.trim());
    }

    public void loginProfile() {
        StringBuilder phone = new StringBuilder("+998");
        System.out.print("Enter your phone -> " + phone);
        phone.append(ComponentContainer.getLine());
//        if (!ComponentContainer.PROFILE_SERVICE.checkPhone()) {
//            System.out.println("Wrong code! Try again");
//            return;
//        }
        System.out.print("Enter your password -> ");
        String password = ComponentContainer.getLine();
        profileService.loginProfile(phone.toString(), password);
    }

    public void changeProfileStatus() {
        System.out.print("Enter phone number -> ");
        String phone = ComponentContainer.getLine();
        ProfileStatus status = profileService.getStatus();
        profileService.changeProfileStatus(phone, status);
    }

    public void showMenu(ProfileDTO profileDTO) {
        int action;
        while (true) {
            try {
                ProfileRole role = profileDTO.getRole();
                showMenu(role);
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (role) {
                    case ADMIN -> {
                        switch (action) {
                            case 1 -> {
                                System.out.println("---- CARD ----");
                                cardController.showCardMenu();
                            }
                            case 2 -> {
                                System.out.println("---- TERMINAL ----");
                                terminalController.showTerminalMenu();
                            }
                            case 3 -> {
                                System.out.println("---- PROFILE ----");
                                showProfileMenu();
                            }
                            case 4 -> {
                                System.out.println("---- TRANSACTION LIST ----");
                                transactionService.showTransactionList();
                            }
                            case 5 -> {
                                System.out.println("---- COMPANY CARD ----");
                                cardService.showCompanyCard();
                            }
                            case 6 -> {
                                System.out.println("---- STATISTICS ----");
                                transactionController.showTransactionMenu();
                            }
                            case 0 -> {
                                System.out.println("Goodbye!!!");
                                return;
                            }
                            default -> System.out.println("\nError [-_-] try again!\n");
                        }
                    }
                    case USER -> {
                        profileDTO = profileService.getProfileByPhone(profileDTO.getPhone());
                        switch (action) {
                            case 1 -> {
                                profileCardController.showMenu(profileDTO);
                            }
                            case 2 -> {
                                transactionController.transactionReFill(profileDTO);
                            }
                            case 3 -> {
                                transactionController.showTransactionList(profileDTO);
                            }
                            case 4 -> {
                                transactionController.transactionPayment(profileDTO);
                            }
                            case 0 -> {
                                System.out.println("Goodbye!!!");
                                return;
                            }
                            default -> System.out.println("\nError [-_-] try again!\n");
                        }
                    }
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void showProfileMenu() {
        int action;
        while (true) {
            try {
                profileMenu();
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (action) {
                    case 1 -> {
                        System.out.println("---- PROFILE LIST ----");
                        profileService.showProfileList();
                    }
                    case 2 -> {
                        System.out.println("---- CHANGE PROFILE STATUS ----");
                        changeProfileStatus();
                    }
                    case 3 -> {
                        System.out.println("---- PROFILE CARD LIST ----");
                        profileCardService.showProfileCardList();
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("\nError [-_-] try again!\n");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void profileMenu() {
        System.out.println("----MENU----");
        System.out.println("1.Profile List");
        System.out.println("2.Change Profile Status");
        System.out.println("2.Profile Cards List");
        System.out.println("0.Back");
    }

    public void showMenu(ProfileRole role) {
        switch (role) {
            case ADMIN -> {
                System.out.println("----MENU----");
                System.out.println("1.Card");
                System.out.println("2.Terminal");
                System.out.println("3.Profile");
                System.out.println("4.Transaction List");
                System.out.println("5.Company Card");
                System.out.println("6.Statistics");
                System.out.println("0.Exit");
            }
            case USER -> {
                System.out.println("----MENU----");
                System.out.println("1.Card");
                System.out.println("2.ReFill Card");
                System.out.println("3.Transaction History");
                System.out.println("4.Make Payment");
                System.out.println("0.Exit");
            }
        }
    }

    public void simpleMenu() {
        int action;
        while (true) {
            try {
                menu();
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (action) {
                    case 1 -> {
                        System.out.println("---- LOGIN ----");
                        loginProfile();
                    }
                    case 2 -> {
                        System.out.println("---- REGISTRATION ----");
                        registrationProfile();
                    }
                    case 0 -> {
                        System.out.println("Goodbye!!!");
                        return;
                    }
                    default -> System.out.println("\nError [-_-] try again!\n");
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void menu() {
        System.out.println("----MENU----");
        System.out.println("1.Login");
        System.out.println("2.Registration");
        System.out.println("0.Quit");
    }
}
