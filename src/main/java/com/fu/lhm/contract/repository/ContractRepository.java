package com.fu.lhm.contract.repository;


import com.fu.lhm.contract.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

     Contract findByTenant_Room_Id(Long roomId);

     List<Contract> findAllByTenant_Room_Id(Long roomId);

     List<Contract> findAllByIsActiveTrue();

     Page<Contract> findAllByTenant_Room_Id(Long id, Pageable page);

     Page<Contract> findAllByTenant_Room_House_Id(Long id, Pageable page);

}
