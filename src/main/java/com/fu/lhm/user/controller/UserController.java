package com.fu.lhm.user.controller;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.AuthenticationResponse;
import com.fu.lhm.user.model.UserRequest;
import com.fu.lhm.user.service.UserService;
import com.fu.lhm.user.model.LoginRequest;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.validate.UserValidate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserValidate userValidate;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }
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

    @PutMapping("")
    public ResponseEntity<String> updateUser(@RequestBody UserRequest newUser
    ) throws BadRequestException {
        userValidate.validateUpdateUser(newUser);
        service.updateUser(newUser);
        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    @GetMapping("")
    public ResponseEntity<UserRequest> getUser() throws BadRequestException {

        return ResponseEntity.ok(service.getUser());
    }


    @PutMapping ("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("password") String password,@RequestParam("newPassword") String newPassword,@RequestParam("repeat") String repeat) throws BadRequestException {
        userValidate.validateChangePassword(password,newPassword,repeat);
        service.changePassword(newPassword);
        return ResponseEntity.ok("Password changed successfully.");
    }

}
