package com.fu.lhm.house.repository;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface HouseRepository extends JpaRepository<House, Long> {
    boolean existsByNameAndUser(String name, User user);
    Integer countByUser(User user);
    List<House> findAllByUser(User user);

    @Query(value = "SELECT * FROM houses b "
            + "WHERE (:userId IS NULL OR b.user_id = :userId) "
            + "AND (:houseId IS NULL OR b.id = :houseId) ",
            nativeQuery = true)
    List<House> findHouses(
            @Param("userId") Long userId,
            @Param("houseId") Long houseId);

    Page<House> findAllByUser(User user, Pageable pageable);

}
