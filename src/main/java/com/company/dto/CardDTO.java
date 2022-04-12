package com.company.dto;

import com.company.enums.CardStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CardDTO {
    private Integer id;
    private String card_number;
    private LocalDate expiration_date;
    private Long balance;
    private CardStatus status;
    private LocalDateTime created_date;
}
