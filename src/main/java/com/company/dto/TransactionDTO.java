package com.company.dto;

import com.company.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TransactionDTO {
    private Integer id;
    private String card_number;
    private Long amount;
    private String terminal_code;
    private TransactionType type;
    private LocalDateTime transaction_date;
}
