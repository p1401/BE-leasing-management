package com.fu.lhm.bill.modal;

import com.fu.lhm.bill.entity.BillType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillSpendRequest {
    private Long id;

    private String billCode;

    private int totalMoney;

    private Date dateCreate;

    private String description;

    //Phieu thu, phieu chi
    @Enumerated(EnumType.STRING)
    private BillType billType;
}
