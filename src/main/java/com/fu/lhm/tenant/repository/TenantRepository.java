package com.fu.lhm.tenant.repository;


import com.fu.lhm.tenant.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Page<Tenant> findAllByRoom_IdAndIsStay(Long RoomId,Boolean isStay, Pageable pageable);

    Page<Tenant> findAllByRoom_IdAndIsStayAndNameContainingIgnoreCase(Long roomId,Boolean isStay,String name, Pageable page);


    Page<Tenant> findAllByRoom_House_IdAndIsStayAndNameContainingIgnoreCase(Long houseId,Boolean isStay,String name, Pageable pagee);


    List<Tenant> findAllByRoom_IdAndIsStayTrue(Long id);

    List<Tenant> findAllByRoom_House_IdAndIsStayTrue(Long id);
}
