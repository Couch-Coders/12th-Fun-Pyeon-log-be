package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    Long userEntryNo;
    String email;
    Date registeredDate;
}
