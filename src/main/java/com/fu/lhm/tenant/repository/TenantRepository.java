package com.fu.lhm.tenant.repository;



import com.fu.lhm.tenant.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant,Long> {

    Page<Tenant> findAllByRoom_Id(Long roomId, Pageable page);

    Page<Tenant> findAllByRoom_House_Id(Long houseId, Pageable pagee);
}
