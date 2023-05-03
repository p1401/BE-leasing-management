package com.fu.lhm.bill.model;

import com.fu.lhm.bill.entity.Bill;
import lombok.*;
import org.springframework.data.domain.Page;

@Generated
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    private long revenue;

    private long receive;

    private long spend;

    private Page<Bill> listBill;

}
