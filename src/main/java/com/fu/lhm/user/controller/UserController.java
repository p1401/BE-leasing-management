package com.fu.lhm.user.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.user.model.AuthenticationResponse;
import com.fu.lhm.user.service.UserService;
import com.fu.lhm.user.model.LoginRequest;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.validate.UserValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserValidate userValidate;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws BadRequestException {
        userValidate.validateCreateUser(request);
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
