package com.fu.lhm.tenant.repository;


import com.fu.lhm.tenant.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

     Contract findByTenant_Room_Id(Long roomId);

     List<Contract> findAllByTenant_Room_Id(Long roomId);

}
