package com.ePatient.Endpoints;

import com.ePatient.Models.LoginModel;
import com.ePatient.Models.SessionModel;
import com.ePatient.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AccountEndpoint {

    private AccountService accountService;

    @Autowired
    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("account/email")
    public ResponseEntity sendEmail(@RequestBody String email) {
        accountService.sendEmail(email);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<SessionModel> signIn(@RequestBody LoginModel loginModel) {
        SessionModel sessionModel = accountService.signIn(loginModel);
        return new ResponseEntity<>(sessionModel, HttpStatus.OK);
    }
}
