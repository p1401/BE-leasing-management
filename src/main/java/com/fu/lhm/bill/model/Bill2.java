package com.fu.lhm.bill.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.contract.entity.Contract;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill2 {

    private Long id;

    private String billCode;

    private String payer;

    private Boolean isPay;

    private LocalDate dateCreate;

    private String description;

    private int totalMoney;

    //Phieu thu, phieu chi
    @Enumerated(EnumType.STRING)
    private BillType billType;

    //Tien phong hoac tien thu them
    @Enumerated(EnumType.STRING)
    private BillContent billContent;

    private String houseName;

    private String roomName;

    private String contractCode;


}
