package com.mdd.pocmdd.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String usernameOrEmail;
    private String password;
}
