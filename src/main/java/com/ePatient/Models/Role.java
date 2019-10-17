package com.ePatient.Models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_PATIENT, ROLE_DOCTOR;

    public String getAuthority() {
        return name();
    }

}
