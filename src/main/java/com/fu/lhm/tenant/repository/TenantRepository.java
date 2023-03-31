package com.fu.lhm.tenant.repository;



import com.fu.lhm.tenant.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant,Long> {

    List<Tenant> findAllByRoom_Id(Long id);
}
