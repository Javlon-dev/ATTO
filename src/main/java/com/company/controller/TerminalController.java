package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.dto.ProfileDTO;
import com.company.enums.CardStatus;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.enums.TerminalStatus;
import com.company.service.TerminalService;
import lombok.Setter;

@Setter
public class TerminalController {

    private TerminalService terminalService;

    public void showTerminalMenu() {
        int action;
        while (true) {
            try {
                terminalMenu();
                System.out.print("Enter your action -> ");
                action = ComponentContainer.getAction();
                switch (action) {
                    case 1 -> {
                        System.out.println("---- CREATE TERMINAL ----");
                        createTerminal();
                    }
                    case 2 -> {
                        System.out.println("---- TERMINAL LIST ----");
                        terminalService.showTerminalList();
                    }
                    case 3 -> {
                        System.out.println("---- UPDATE TERMINAL ----");
                        updateTerminal();
                    }
                    case 4 -> {
                        System.out.println("---- CHANGE TERMINAL STATUS ----");
                        changeTerminalStatus();
                    }
                    case 5 -> {
                        System.out.println("---- DELETE TERMINAL ----");
                        deleteTerminal();
                    }
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("\nError [-_-] try again!\n");
                }
            } catch (
                    RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTerminal() {
        System.out.print("Enter Terminal address -> ");
        String address = ComponentContainer.getLine();
        terminalService.createTerminal(address.trim());
    }

    public void updateTerminal() {
        if (!terminalService.showTerminalList()) {
            return;
        }
        System.out.print("Enter Terminal code -> ");
        String code = ComponentContainer.getLine();
        System.out.print("Enter Terminal address -> ");
        String address = ComponentContainer.getLine();
        terminalService.updateTerminal(code, address.trim());
    }

    public void changeTerminalStatus() {
        if (!terminalService.showTerminalList()) {
            return;
        }
        System.out.print("Enter Terminal code -> ");
        String code = ComponentContainer.getLine();
        TerminalStatus status = terminalService.getStatus();
        terminalService.changeTerminalStatus(code, status);
    }

    public void deleteTerminal() {
        if (!terminalService.showTerminalList()) {
            return;
        }
        System.out.print("Enter Terminal code -> ");
        String code = ComponentContainer.getLine();
        terminalService.deleteTerminal(code);
    }

    private void terminalMenu() {
        System.out.println("----MENU----");
        System.out.println("1.Create Terminal");
        System.out.println("2.Terminal List");
        System.out.println("3.Update Terminal");
        System.out.println("4.Change Terminal status");
        System.out.println("5.Delete Terminal");
        System.out.println("0.Back");
    }

}
