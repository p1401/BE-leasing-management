package com.fu.lhm.bill.repository;

import com.fu.lhm.bill.Bill;
import com.fu.lhm.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable pageable);

    List<Bill> findAllByIsPayFalse();

}
