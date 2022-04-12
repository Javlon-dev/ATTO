package com.company.service;

import com.company.container.ComponentContainer;
import com.company.dto.CardDTO;
import com.company.dto.TerminalDTO;
import com.company.enums.CardStatus;
import com.company.enums.TerminalStatus;
import com.company.repository.TerminalRepository;
import lombok.Setter;
import org.postgresql.util.internal.Nullness;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Setter
public class TerminalService {

    private TerminalRepository terminalRepository;


    public void createTerminal(String address) {
        TerminalDTO terminalDTO = new TerminalDTO();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            code.append((int) (Math.random() * 10));
        }
        terminalDTO.setCode(code.toString());
        terminalDTO.setAddress(address);
        terminalDTO.setStatus(TerminalStatus.ACTIVE);
        terminalDTO.setCreated_date(LocalDateTime.now());
        TerminalDTO dto = terminalRepository.createTerminal(terminalDTO);
        Optional<TerminalDTO> optional = Optional.ofNullable(dto);
        if (optional.isPresent()) {
            System.out.println(terminalDTO);
            System.out.println("Successfully created!");
        } else {
            System.out.println("Mazgi, try again!");
        }
    }

    public boolean showTerminalList() {
        List<TerminalDTO> dtoList = terminalRepository.getTerminalList();
        if (dtoList.isEmpty()) {
            System.out.println("You have not terminals, Mazgiable");
            return false;
        }
        for (TerminalDTO dto : dtoList) {
            System.out.println(dto);
        }
        return true;
    }

    public void updateTerminal(String code, String address) {
        if (getTerminalByCode(code) == null){
            return;
        }
        if (address.isEmpty()){
            System.out.println("Unknown address! Mazgiable");
            return;
        }
        TerminalDTO terminalDTO = terminalRepository.updateTerminal(code,address);
        Optional<TerminalDTO> dto = Optional.ofNullable(terminalDTO);
        if (dto.isPresent()) {
            System.out.println(terminalDTO);
            System.out.println("Successfully updated!");
        }else System.out.println("Mazgi nitoku! try again");
    }

    public void changeTerminalStatus(String code, TerminalStatus status) {
        if (getTerminalByCode(code) == null){
            return;
        }
        TerminalDTO terminalDTO = terminalRepository.changeCardStatus(code, status);
        Optional<TerminalDTO> optional = Optional.ofNullable(terminalDTO);
        if (optional.isPresent()) {
            System.out.println(terminalDTO);
            System.out.println("Successfully changed!");
        }else System.out.println("Mazgi nitoku! try again");
    }

    public void deleteTerminal(String code) {
        if (getTerminalByCode(code) == null){
            return;
        }
        if (terminalRepository.deleteTerminal(code) > 0) {
            System.out.println("Successfully deleted!");
        }else System.out.println("Mazgi nitoku! try again");
    }

    public TerminalDTO getTerminalByCode(String terminal_code) {
        TerminalDTO terminalDTO = terminalRepository.getTerminalByCode(terminal_code);
        Optional<TerminalDTO> optional = Optional.ofNullable(terminalDTO);
        if (optional.isEmpty()) {
            System.out.println("Wrong terminal code! Mazgi");
            return null;
        }
        return terminalDTO;
    }

    public TerminalStatus getStatus() {
        int action;
        while (true) {
            statusMenu();
            System.out.print("Enter your action -> ");
            action = ComponentContainer.getAction();
            switch (action) {
                case 1 -> {
                    return TerminalStatus.ACTIVE;
                }
                case 2 -> {
                    return TerminalStatus.BLOCK;
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
