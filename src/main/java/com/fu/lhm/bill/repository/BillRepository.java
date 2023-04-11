package com.fu.lhm.bill.repository;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable pageable);

    Page<Bill> findAllByRoomId(Long roomId, Pageable pageable);

    List<Bill> findAllByIsPayFalse();

    List<Bill> findByContract_Tenant_Room_IdAndBillTypeAndBillContentAndDateCreateBetween(Long roomId, BillType billType, BillContent billContent, LocalDate startDate, LocalDate endDate);

    List<Bill> findAllByRoomIdAndBillType(Long roomId, BillType billType);
}

//    List<Bill> findByContract_Tenant_Room_IdAndDateCreateBetween(Long id, LocalDate startDate, LocalDate endDate);


