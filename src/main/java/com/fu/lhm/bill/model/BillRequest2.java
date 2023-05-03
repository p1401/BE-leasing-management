package com.fu.lhm.bill.model;

import com.fu.lhm.bill.entity.Bill;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest2 {
    private long revenue;

    private long receive;

    private long spend;

    private Page<Bill2> listBill;
}
