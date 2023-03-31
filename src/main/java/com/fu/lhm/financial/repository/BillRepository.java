package com.fu.lhm.financial.repository;

import com.fu.lhm.financial.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill,Long> {
}
