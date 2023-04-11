package com.fu.lhm.house.repository;

import com.fu.lhm.house.entity.House;
import com.fu.lhm.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HouseRepository extends JpaRepository<House, Long> {
    boolean existsByNameAndUser(String name, User user);

    Integer countByUser(User user);

    List<House> findAllByUser(User user);
    Page<House> findAllByUser(User user, Pageable pageable);

}
