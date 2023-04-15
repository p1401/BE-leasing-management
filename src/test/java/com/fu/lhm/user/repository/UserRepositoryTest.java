package com.fu.lhm.user.repository;

import com.fu.lhm.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    private UserRepository testUserRepository;

    @Test
    void shouldHaveUser(){

        User user = new User();
        user.setName("Ngô Minh Hoàng");
        user.setAddress("Xa La");
        user.setEmail("ngominhoang190@gmail.com");
        user.setPassword("123");
        testUserRepository.save(user);
    }

}