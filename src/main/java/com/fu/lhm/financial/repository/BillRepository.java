package com.fu.lhm.financial.repository;

import com.fu.lhm.financial.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {

    List<Bill> findAllByContract_Tenant_Room_IdAAndBillTypeReceive(Long roomId);

    Page<Bill> findAllByContract_Tenant_Room_Id(Long roomId, Pageable page);
    Page<Bill> findByPayFalseAndContract_Tenant_Room_id( Long roomId, Pageable page);
    Page<Bill> findAllByContract_Tenant_Room_House_Id(Long houseId, Pageable page);

}
