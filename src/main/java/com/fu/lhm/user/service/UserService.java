package com.fu.lhm.user.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.jwt.entity.Token;
import com.fu.lhm.jwt.repository.TokenRepository;
import com.fu.lhm.jwt.entity.TokenType;
import com.fu.lhm.user.entity.Role;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.AuthenticationResponse;
import com.fu.lhm.user.model.LoginRequest;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.model.UserRequest;
import com.fu.lhm.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final HttpServletRequest httpServletRequest;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void updateUser(UserRequest user) throws BadRequestException {
        User oldUser = getUserToken();
        oldUser.setName(user.getName());
        oldUser.setAddress(user.getAddress());
        oldUser.setBirth(user.getBirth());
        oldUser.setPhone(user.getPhone());
        oldUser.setIdentityNumber(user.getIdentityNumber());
        repository.save(oldUser);

    }

    public void changePassword(String newPassword) throws BadRequestException {
        User user = getUserToken();
        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    public UserRequest getUser() throws BadRequestException {
        User user = getUserToken();
        UserRequest userRequest = new UserRequest();
        userRequest.setId(user.getId());
        userRequest.setEmail(user.getEmail());
        userRequest.setName(user.getName());
        userRequest.setAddress(user.getAddress());
        userRequest.setBirth(user.getBirth());
        userRequest.setIdentityNumber(user.getIdentityNumber());
        userRequest.setPhone(user.getPhone());
        userRequest.setPassword(userRequest.getPassword());
        return  userRequest;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
