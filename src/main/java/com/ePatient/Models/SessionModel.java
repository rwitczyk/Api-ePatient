package com.ePatient.Models;

import lombok.Data;

@Data
public class SessionModel {
    private int accountId;
    private Role role;
    private String jwtToken;
}
