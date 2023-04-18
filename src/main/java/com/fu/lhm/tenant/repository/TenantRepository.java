package com.fu.lhm.tenant.repository;


import com.fu.lhm.tenant.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Page<Tenant> findAllByRoom_IdAndIsStay(Long roomId,Boolean isStay, Pageable page);


    Page<Tenant> findAllByRoom_House_IdAndIsStay(Long houseId,Boolean isStay, Pageable pagee);


    List<Tenant> findAllByRoom_IdAndIsStayTrue(Long id);

}
