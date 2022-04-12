package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileRole;
import com.company.service.CardService;
import lombok.Setter;

@Setter
public class CardController {

    private CardService cardService;


    public void showCardMenu() {
        int action;
        while (true) {
            try {
                cardMenu();
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (action) {
                    case 1 -> {
                        System.out.println("---- CREATE CARD ----");
                        cardService.createCard();
                    }
                    case 2 -> {
                        System.out.println("---- CARD LIST ----");
                        cardService.showCardList();
                    }
                    case 3 -> {
                        System.out.println("---- UPDATE CARD ----");
                        updateCard();
                    }
                    case 4 -> {
                        System.out.println("---- CHANGE CARD STATUS ----");
                        changeCardStatus();
                    }
                    case 5 -> {
                        System.out.println("---- REFILL ----");
                        cardRefill();
                    }
                    case 6 -> {
                        System.out.println("---- DELETE CARD ----");
                        deleteCard();
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

    public void cardMenu() {
        System.out.println("----MENU----");
        System.out.println("1.Create Card");
        System.out.println("2.Card List");
        System.out.println("3.Update Card");
        System.out.println("4.Change Card status");
        System.out.println("5.ReFill");
        System.out.println("6.Delete");
        System.out.println("0.Back");
    }

    public void updateCard() {
        if (!cardService.showCardList()) {
            return;
        }
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        System.out.print("Enter expiration date (yyyy-MM-dd) -> ");
        String date = ComponentContainer.getLine();
        cardService.updateCard(card_number, date);
    }

    public void changeCardStatus() {
        if (!cardService.showCardList()) {
            return;
        }
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        CardStatus status = cardService.getStatus();
        cardService.changeCardStatus(card_number, status);
    }

    public void cardRefill() {
        if (!cardService.showCardList()) {
            return;
        }
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        System.out.print("Payment balance -> ");
        Long balance = ComponentContainer.SCANNER.nextLong();
        cardService.cardRefill(card_number, balance);
    }

    public void deleteCard() {
        if (!cardService.showCardList()) {
            return;
        }
        System.out.print("Enter card number -> ");
        String card_number = ComponentContainer.getLine();
        cardService.deleteCard(card_number);
    }

}
