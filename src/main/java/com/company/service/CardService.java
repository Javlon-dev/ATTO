package com.company.service;

import com.company.container.ComponentContainer;
import com.company.dto.CardDTO;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.repository.CardRepository;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Setter
public class CardService {

    CardRepository cardRepository;

    public void createCard() {
        StringBuilder card_number = new StringBuilder("86001234");
        for (int i = 0; i < 8; i++) {
            card_number.append((int) (Math.random() * 10));
        }
        CardDTO cardDTO = new CardDTO();
        cardDTO.setCard_number(card_number.toString());
        cardDTO.setExpiration_date(LocalDate.now().plusYears(ComponentContainer.EXPIRATION_DATE));
        cardDTO.setBalance(0L);
        cardDTO.setStatus(CardStatus.ACTIVE);
        cardDTO.setCreated_date(LocalDateTime.now());

        CardDTO dto = cardRepository.createCard(cardDTO);
        Optional<CardDTO> optional = Optional.ofNullable(dto);
        if (optional.isPresent()) {
            System.out.println(cardDTO);
            System.out.println("Successfully created!");
        } else {
            System.out.println("Mazgi, try again!");
        }
    }

    public boolean showCardList() {
        List<CardDTO> dtoList = cardRepository.getCardList();
        if (dtoList.isEmpty()) {
            System.out.println("You have not cards!, Mazgiable");
            return false;
        }
        for (CardDTO dto : dtoList) {
            System.out.println(dto);
        }
        return true;
    }

    public void updateCard(String card_number, String date) {
        if (getCardByNumber(card_number) == null) {
            return;
        }
        LocalDate expiration_date = LocalDate.parse(date);
        CardDTO cardDTO = cardRepository.updateCard(card_number, expiration_date);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isPresent()) {
            System.out.println(cardDTO);
            System.out.println("Successfully updated!");
        } else System.out.println("Mazgi nitoku! try again");
    }

    public void deleteCard(String card_number) {
        if (getCardByNumber(card_number) == null) {
            return;
        }
        if (cardRepository.deleteCard(card_number) > 0) {
            System.out.println("Successfully deleted!");
        }
    }

    public CardDTO getCardByNumber(String card_number) {
        CardDTO cardDTO = cardRepository.getCardByNumber(card_number);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isEmpty()) {
            System.out.println("Wrong card number! Mazgi");
            return null;
        }
        return cardDTO;
    }

    public void changeCardStatus(String card_number, CardStatus status) {
        if (getCardByNumber(card_number) == null) {
            return;
        }
        CardDTO cardDTO = cardRepository.changeCardStatus(card_number, status);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isPresent()) {
            System.out.println(cardDTO);
            System.out.println("Successfully changed!");
        } else System.out.println("Mazgi nitoku! try again");
    }

    public void cardRefill(String card_number, Long balance) {
        if (getCardByNumber(card_number) == null) {
            return;
        }
        CardDTO cardDTO = cardRepository.cardRefill(card_number, balance);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isPresent()) {
            System.out.println(cardDTO);
            System.out.println("Successfully ReFilled!");
        } else System.out.println("Mazgi nitoku! try again");
    }

    public void showCompanyCard() {
        CardDTO cardDTO = cardRepository.getCardByNumber(ComponentContainer.COMPANY_CARD);
        Optional<CardDTO> optional = Optional.ofNullable(cardDTO);
        if (optional.isEmpty()) {
            System.out.println("Wrong card number! Mazgi");
            return;
        }
        System.out.println(cardDTO);
    }

    public CardStatus getStatus() {
        int action;
        while (true) {
            statusMenu();
            System.out.print("Enter your action -> ");
            action = ComponentContainer.getAction();
            switch (action) {
                case 1 -> {
                    return CardStatus.ACTIVE;
                }
                case 2 -> {
                    return CardStatus.BLOCK;
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
