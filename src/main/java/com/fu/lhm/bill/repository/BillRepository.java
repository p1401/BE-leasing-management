package com.fu.lhm.bill.repository;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable pageable);

    Page<Bill> findAllByRoomId(Long roomId, Pageable pageable);

    Page<Bill> findAllByHouseId(Long houseId, Pageable pageable);

    List<Bill> findAllByIsPayFalse();

    List<Bill> findByContract_Tenant_Room_IdAndBillTypeAndBillContentAndDateCreateBetween(Long roomId, BillType billType, BillContent billContent, LocalDate startDate, LocalDate endDate);

    List<Bill> findAllByRoomIdAndBillType(Long roomId, BillType billType);

    List<Bill> findAllByContract_Tenant_Room_House_User(User user);

    List<Bill> findAllByContract_Tenant_Room_House_Id(Long houseId);

//    @Query(value = "SELECT * FROM ticket "
//            + "WHERE (:isPlus IS NULL OR IS_PLUS = :isPlus) "
//            + "AND (:fromDateOfTrans IS NULL OR DATE_OF_TRANS >= :fromDateOfTrans) "
//            + "AND (:toDateOfTrans IS NULL OR DATE_OF_TRANS <= :toDateOfTrans) ",
//            nativeQuery = true)
//    List<Bill> findList(
//
//            @Param("fromDate") Instant fromDateOfTrans,
//            @Param("toDate") Instant toDateOfTrans);
//


}



