package com.company.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileCardDTO {
    private Integer id;
    private String card_number;
    private String phone;
    private LocalDateTime added_date;
    private Boolean visible_user;
}
