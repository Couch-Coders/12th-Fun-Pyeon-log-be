package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserDTO {
    Long userEntryNo;
    String email;
    LocalDateTime registeredDate;
}
