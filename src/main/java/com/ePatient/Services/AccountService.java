package com.ePatient.Services;

import com.ePatient.Models.LoginModel;
import com.ePatient.Models.SessionModel;

public interface AccountService {
    void sendEmail(String email);

    SessionModel signIn(LoginModel loginModel);
}

