package com.fu.lhm.financial.repository;

import com.fu.lhm.financial.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {

    List<Bill> findAllByContract_Tenant_Room_Id(Long roomId);


}
