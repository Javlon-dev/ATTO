package com.company.dto;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String phone;
    private String password;
    private LocalDateTime created_date;
    private ProfileStatus status;
    private ProfileRole role;
}
