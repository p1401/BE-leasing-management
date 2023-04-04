package com.fu.lhm.house.repository;

import com.fu.lhm.house.House;
import com.fu.lhm.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HouseRepository extends JpaRepository<House, Long> {
    boolean existsByNameAndUser(String name, User user);

    Page<House> findAllByUser(User user, Pageable pageable);

    List<House> findAllByUser(User user);
}
