package com.fu.lhm.user.service;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.jwt.entity.Token;
import com.fu.lhm.jwt.entity.TokenType;
import com.fu.lhm.jwt.repository.TokenRepository;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.Role;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.AuthenticationResponse;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.model.UserRequest;
import com.fu.lhm.user.repository.UserRepository;
import com.fu.lhm.user.service.UserService;
import com.fu.lhm.user.validate.UserValidate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidate userValidate;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    public void register() {
        // Create a RegisterRequest object with test data
        RegisterRequest request = new RegisterRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        // Create a User object with the same test data
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();

        // Create a JWT token
        String jwtToken = "jwtToken123";
        Token token = new Token(1L,jwtToken, TokenType.BEARER,true,true, user);

        // Mock the UserRepository's save method to return the saved User object
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        // Mock the JwtService's generateToken method to return the JWT token
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        // Call the register method with the test RegisterRequest object
        AuthenticationResponse response = userService.register(request);

        // Verify that the AuthenticationResponse object contains the correct JWT token
        Assert.assertEquals(jwtToken, response.getToken());
    }

    @Test()
    public void testCreateHouse_InvalidData_Name() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập họ tên đầy đủ", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Email() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập Email", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Email2() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("xxx");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Email không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Password() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập password", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Address() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxx");
        request.setAddress("");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập địa chỉ", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Phone() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxx");
        request.setAddress("xcxxxx");
        request.setPhone("");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(Role.USER)
                .address(request.getAddress())
                .phone(request.getPhone())
                .identityNumber(request.getIdentityNumber())
                .birth(request.getBirth())
                .build();
        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập số điện thoại", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_Phone2() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxx");
        request.setAddress("xcxxxx");
        request.setPhone("cz");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Số điện thoại không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testCreateHouse_InvalidData_IdentityNumber() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxx");
        request.setAddress("xcxxxx");
        request.setPhone("0904270666");
        request.setIdentityNumber("");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateCreateUser(request);
        });
        Assert.assertEquals("Nhập CMND", exception.getMessage());
    }

    @Test
    public void authenticate() throws BadRequestException {

    }

    @Test
    public void updateUser() throws BadRequestException {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setAddress("123 Main St");
        userRequest.setBirth(new Date());
        userRequest.setPhone("123-456-7890");
        userRequest.setIdentityNumber("ABC123");

        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setName("Jane Doe");
        oldUser.setAddress("456 Oak St");
        oldUser.setBirth(new Date());
        oldUser.setPhone("555-555-1212");
        oldUser.setIdentityNumber("XYZ789");

        when(userService.getUserToken()).thenReturn(oldUser);
        when(userRepository.save(oldUser)).thenReturn(oldUser);

        userService.updateUser(userRequest);


        verify(userRepository, times(1)).save(oldUser);

        assertEquals(userRequest.getName(), oldUser.getName());
        assertEquals(userRequest.getAddress(), oldUser.getAddress());
        assertEquals(userRequest.getBirth(), oldUser.getBirth());
        assertEquals(userRequest.getPhone(), oldUser.getPhone());
        assertEquals(userRequest.getIdentityNumber(), oldUser.getIdentityNumber());
    }

    @Test()
    public void testUpdateHouse_InvalidData_Name() {
        // given
        UserRequest request = new UserRequest();
        request.setName("");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        Assert.assertEquals("Nhập họ tên đầy đủ", exception.getMessage());
    }

    @Test()
    public void testUpdateUser_InvalidData_Email() {
        // given
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });

        Assert.assertEquals("Email không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testUpdateUser_InvalidData_Email2() {
        // given
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("xxx");
        request.setPassword("password123");
        request.setAddress("123 Main St.");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        Assert.assertEquals("Email không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testUpdate_InvalidData_Address() {
        // given
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxxx");
        request.setAddress("");
        request.setPhone("555-1234");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        //when

        Assert.assertEquals("Nhập địa chỉ", exception.getMessage());
    }

    @Test()
    public void testUpdateUser_InvalidData_Phone() {
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxxx");
        request.setAddress("asdasd");
        request.setPhone("");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        Assert.assertEquals("Nhập số điện thoại", exception.getMessage());
    }

    @Test()
    public void testUpdateUser_InvalidData_Phone2() {
        // given
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxxx");
        request.setAddress("asdasd");
        request.setPhone("xxx");
        request.setIdentityNumber("123456789");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        Assert.assertEquals("Số điện thoại không đúng định dạng!", exception.getMessage());
    }

    @Test()
    public void testUpdate_InvalidData_IdentityNumber() {
        // given
        UserRequest request = new UserRequest();
        request.setName("xxx");
        request.setEmail("john.doe@example.com");
        request.setPassword("xxxx");
        request.setAddress("asdasd");
        request.setPhone("xxx");
        request.setIdentityNumber("");
        request.setBirth(new Date());

        //when
        BadRequestException exception = Assert.assertThrows(BadRequestException.class, () -> {
            userValidate.validateUpdateUser(request);
        });
        Assert.assertEquals("Nhập CMND", exception.getMessage());
    }

    @Test
    public void changePassword() throws BadRequestException {
        // Create a new password
        String newPassword = "newpassword123";

        // Create a mock user object
        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .role(Role.USER)
                .address("123 Main St.")
                .phone("555-1234")
                .identityNumber("123456789")
                .birth(new Date())
                .build();

        // Mock the getUserToken method to return the mock user object
        when(userService.getUserToken()).thenReturn(user);

        // Call the changePassword method with the new password
        userService.changePassword(newPassword);

        // Verify that the user's password was updated with the encoded new password
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        Assert.assertEquals(passwordEncoder.encode(newPassword), capturedUser.getPassword());
    }


    @Test
    public void getUser() throws BadRequestException {
        // Create a mock user object with test data
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .address("123 Main St.")
                .phone("555-1234")
                .identityNumber("123456789")
                .birth(new Date())
                .build();

        // Mock the UserService's getUserToken method to return the mock user object
        when(userService.getUserToken()).thenReturn(user);

        // Call the getUser method
        UserRequest userRequest = userService.getUser();

        // Verify that the UserRequest object contains the correct data
        Assert.assertEquals(user.getId(), userRequest.getId());
        Assert.assertEquals(user.getName(), userRequest.getName());
        Assert.assertEquals(user.getEmail(), userRequest.getEmail());
        Assert.assertEquals(user.getAddress(), userRequest.getAddress());
        Assert.assertEquals(user.getBirth(), userRequest.getBirth());
        Assert.assertEquals(user.getIdentityNumber(), userRequest.getIdentityNumber());
        Assert.assertEquals(user.getPhone(), userRequest.getPhone());

    }
}