package com.fu.lhm.bill.repository;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable pageable);

    Page<Bill> findAllByRoomId(Long roomId, Pageable pageable);

    Page<Bill> findAllByHouseId(Long houseId, Pageable pageable);

    List<Bill> findAllByIsPayFalse();

    List<Bill> findByContract_Tenant_Room_IdAndBillTypeAndBillContentAndDateCreateBetween(Long roomId, BillType billType, BillContent billContent, LocalDate startDate, LocalDate endDate);

    List<Bill> findAllByRoomIdAndBillType(Long roomId, BillType billType);

    List<Bill> findAllByUser(User user);

//    List<Bill> findAllByHouseId(House house);
//
//    List<Bill> findAllByContract_Tenant_Room_House_Id(Long houseId);

    @Query(value = "SELECT * FROM bills b "
            + "WHERE (:userId IS NULL OR b.user_id = :userId) "
            + "AND (:houseId IS NULL OR b.house_id = :houseId) "
            + "AND (:roomId IS NULL OR b.room_id = :roomId) "
            + "AND (:fromDate IS NULL OR b.date_create >= :fromDate) "
            + "AND (:toDate IS NULL OR b.date_create <= :toDate) "
            + "AND (:billType IS NULL OR b.bill_type = :billType) "
            + "AND (:isPay IS NULL OR b.is_pay = :isPay) ",
            nativeQuery = true)
    Page<Bill> findBills(
            @Param("userId") Long userId,
            @Param("houseId") Long houseId,
            @Param("roomId") Long roomId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("billType") String billType,
            @Param("isPay") Boolean isPay,
            Pageable page);

}



