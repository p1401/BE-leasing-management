package com.fu.lhm.house.Repository;

import com.fu.lhm.house.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface HouseRepository extends JpaRepository<House,Long> {

    List<House> findAllByEmailUserIgnoreCase(String emailUser);
}
