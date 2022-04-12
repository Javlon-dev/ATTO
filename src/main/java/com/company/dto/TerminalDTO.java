package com.company.dto;

import com.company.enums.TerminalStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminalDTO {
    private Integer id;
    private String code;
    private String address;
    private TerminalStatus status;
    private LocalDateTime created_date;
}
