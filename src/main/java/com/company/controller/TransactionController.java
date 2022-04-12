package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.dto.ProfileDTO;
import com.company.enums.ProfileStatus;
import com.company.service.CardService;
import com.company.service.ProfileCardService;
import com.company.service.TransactionService;
import lombok.Setter;

@Setter
public class TransactionController {

    private ProfileCardService profileCardService;
    private TransactionService transactionService;
    private CardService cardService;


    public void transactionReFill(ProfileDTO profileDTO) {
        if (profileDTO.getStatus().equals(ProfileStatus.BLOCK)) {
            System.out.println("User is blocked! please Mazgi qilmen");
            return;
        }
        profileCardService.showProfileCardList(profileDTO);
        System.out.print("Enter your card number -> ");
        String card_number = ComponentContainer.getLine();
        System.out.print("Payment balance -> ");
        Long balance = ComponentContainer.SCANNER.nextLong();
        transactionService.transactionReFill(profileDTO, card_number, balance);
    }

    public void showTransactionList(ProfileDTO profileDTO) {
        transactionService.showTransactionList(profileDTO);
    }

    public void transactionPayment(ProfileDTO profileDTO) {
        if (profileDTO.getStatus().equals(ProfileStatus.BLOCK)) {
            System.out.println("User is blocked! please Mazgi qilmen");
            return;
        }
        profileCardService.showProfileCardList(profileDTO);
        System.out.print("Enter your card number -> ");
        String card_number = ComponentContainer.getLine();
        System.out.print("Enter terminal code -> ");
        String terminal_code = ComponentContainer.getLine();
        transactionService.transactionPayment(profileDTO, card_number, terminal_code);
    }


    public void showTransactionMenu() {
        int action;
        while (true) {
            try {
                transactionMenu();
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (action) {
                    case 1 -> {
                        System.out.println("---- TODAY TRANSACTIONS ----");
                        transactionService.showTodayTransaction();
                    }
                    case 2 -> {
                        System.out.println("---- DAILY TRANSACTIONS ----");
                        showTransactionDay();
                    }
                    case 3 -> {
                        System.out.println("---- BETWEEN DAYS TRANSACTIONS ----");
                        showTransactionBetweenDay();
                    }
                    case 4 -> {
                        System.out.println("---- COMPANY CARD ----");
                        cardService.showCompanyCard();
                    }
                    case 5 -> {
                        System.out.println("---- TERMINAL TRANSACTIONS ----");
                        showTransactionByTerminal();
                    }
                    case 6 -> {
                        System.out.println("---- CARD TRANSACTIONS ----");
                        showTransactionByCard();
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

    public void showTransactionByTerminal() {
        System.out.print("Enter Terminal Code -> ");
        String terminal_code = ComponentContainer.getLine();
        transactionService.showTransactionByTerminal(terminal_code);
    }

    public void showTransactionByCard() {
        System.out.print("Enter Card Number -> ");
        String card_number = ComponentContainer.getLine();
        transactionService.showTransactionByCard(card_number);
    }

    public void showTransactionBetweenDay() {
        System.out.print("Enter from date (yyyy-MM-dd) -> ");
        String from_date = ComponentContainer.getLine();
        System.out.print("Enter to date (yyyy-MM-dd) -> ");
        String to_date = ComponentContainer.getLine();
        transactionService.showTransactionBetweenDay(from_date, to_date);
    }

    public void showTransactionDay() {
        System.out.print("Enter date (yyyy-MM-dd) -> ");
        String date = ComponentContainer.getLine();
        transactionService.showTransactionDay(date);
    }

    public void transactionMenu() {
        System.out.println("----MENU----");
        System.out.println("1.Today Transactions");
        System.out.println("2.Daily Transactions");
        System.out.println("3.Transactions Between Days");
        System.out.println("4.Show Company Card");
        System.out.println("5.Transactions By Terminal");
        System.out.println("6.Transactions By Card");
        System.out.println("0.Back");
    }
}
