package com.fu.lhm.financial.repository;

import com.fu.lhm.financial.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable pageable);



}
