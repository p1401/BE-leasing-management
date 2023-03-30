package com.example.demo.house.Repository;

import com.example.demo.house.House;
import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface HouseRepository extends JpaRepository<House,Long> {

    List<House> findByEmailUser(String userEmail);
}
