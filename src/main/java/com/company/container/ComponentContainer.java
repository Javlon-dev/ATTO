package com.company.container;

import java.util.Scanner;

public interface ComponentContainer {

    Integer EXPIRATION_DATE = 2;
    Long TERMINAL_AMOUNT = 1400L;
    String COMPANY_CARD = "8600123477777777";

    Scanner SCANNER = new Scanner(System.in);

    static int getAction() {
        return SCANNER.nextInt();
    }

    static String getLine() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

}
