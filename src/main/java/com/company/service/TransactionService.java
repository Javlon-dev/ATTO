package com.company.service;

import com.company.container.ComponentContainer;
import com.company.dto.*;
import com.company.enums.CardStatus;
import com.company.enums.TerminalStatus;
import com.company.enums.TransactionType;
import com.company.repository.CardRepository;
import com.company.repository.ProfileCardRepository;
import com.company.repository.TransactionRepository;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Setter
public class TransactionService {

    private CardService cardService;
    private CardRepository cardRepository;
    private ProfileCardRepository profileCardRepository;
    private TransactionRepository transactionRepository;
    private TerminalService terminalService;

    public void transactionReFill(ProfileDTO profileDTO, String card_number, Long balance) {
        CardDTO cardDTO = cardRepository.getCardByNumber(card_number);
        ProfileCardDTO profileCardDTO = profileCardRepository.getProfileCardByNumber(profileDTO, card_number);
        if (profileCardDTO == null) {
            System.out.println("Wrong card number! try again");
            return;
        }
        if (balance <= 0L) {
            System.out.println("MAZGI balance -0ku, testirovchikmisan?");
            return;
        }
        if (cardDTO.getStatus().equals(CardStatus.BLOCK)) {
            System.out.println("Card is blocked!");
            return;
        }
        TransactionDTO dto = new TransactionDTO();
        dto.setCard_number(card_number);
        dto.setAmount(balance);
        dto.setType(TransactionType.REFILL);
        dto.setTransaction_date(LocalDateTime.now());
        TransactionDTO historyDTO = transactionRepository.transactionReFill(dto);
        Optional<TransactionDTO> optional = Optional.ofNullable(historyDTO);
        if (optional.isPresent()) {
            System.out.println(historyDTO);
            System.out.println("Successfully ReFilled!");
        } else System.out.println("Mazgi nitoku! try again");
    }

    public void showTransactionList(ProfileDTO profileDTO) {
        List<TransactionDTO> list = transactionRepository.showTransactionList(profileDTO);
        if (list.isEmpty()) {
            System.out.println("You have not transactions, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void showTransactionList() {
        List<TransactionDTO> list = transactionRepository.showTransactionList();
        if (list.isEmpty()) {
            System.out.println("You have not transactions, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void transactionPayment(ProfileDTO profileDTO, String card_number, String terminal_code) {
        CardDTO cardDTO = cardService.getCardByNumber(card_number);
        ProfileCardDTO profileCardDTO = profileCardRepository.getProfileCardByNumber(profileDTO, card_number);
        TerminalDTO terminalDTO = terminalService.getTerminalByCode(terminal_code);
        if (profileCardDTO == null) {
            System.out.println("Wrong card number! try again");
            return;
        }
        if (cardDTO == null) {
            return;
        }
        if (cardDTO.getStatus().equals(CardStatus.BLOCK)) {
            System.out.println("Card is blocked!");
            return;
        }
        if (cardDTO.getBalance() < 1400L) {
            System.out.println("No money, no transport! ReFill your balance!");
            return;
        }
        if (terminalDTO == null) {
            return;
        }
        if (terminalDTO.getStatus().equals(TerminalStatus.BLOCK)){
            System.out.println("Terminal is blocked! please choose another terminal, mazgi");
            return;
        }
        TransactionDTO dto = new TransactionDTO();
        dto.setCard_number(card_number);
        dto.setAmount(ComponentContainer.TERMINAL_AMOUNT);
        dto.setTerminal_code(terminal_code);
        dto.setType(TransactionType.PAYMENT);
        dto.setTransaction_date(LocalDateTime.now());
        TransactionDTO historyDTO = transactionRepository.transactionPayment(dto);
        Optional<TransactionDTO> optional = Optional.ofNullable(historyDTO);
        if (optional.isPresent()) {
            System.out.println(historyDTO);
            System.out.println("Successfully Payment!");
        } else System.out.println("Mazgi nitoku! try again");
    }

    public void showTodayTransaction() {
        List<TransactionDTO> list = transactionRepository.getTransactionByDay(LocalDate.now());
        if (list.isEmpty()) {
            System.out.println("Transaction yo'q, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void showTransactionDay(String date) {
        LocalDate day = LocalDate.parse(date);
        List<TransactionDTO> list = transactionRepository.getTransactionByDay(day);
        if (list.isEmpty()) {
            System.out.println("Transaction yo'q, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void showTransactionBetweenDay(String from_date, String to_date) {
        LocalDate from_day = LocalDate.parse(from_date);
        LocalDate to_day = LocalDate.parse(to_date);
        List<TransactionDTO> list = transactionRepository.getTransactionBetweenDay(from_day, to_day);
        if (list.isEmpty()) {
            System.out.println("Transaction yo'q, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void showTransactionByTerminal(String terminal_code) {
        if (terminalService.getTerminalByCode(terminal_code) == null) {
            return;
        }
        List<TransactionDTO> list = transactionRepository.getTransactionByTerminal(terminal_code);
        if (list.isEmpty()) {
            System.out.println("Transaction yo'q, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }

    public void showTransactionByCard(String card_number) {
        if (cardService.getCardByNumber(card_number) == null) {
            return;
        }
        List<TransactionDTO> list = transactionRepository.getTransactionByCard(card_number);
        if (list.isEmpty()) {
            System.out.println("Transaction yo'q, Mazgiable");
            return;
        }
        for (TransactionDTO dto : list) {
            System.out.println(dto);
        }
    }
}
