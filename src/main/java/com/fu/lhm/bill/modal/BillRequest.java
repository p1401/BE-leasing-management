package com.fu.lhm.bill.modal;

import com.fu.lhm.bill.entity.BillContent;
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
public class BillRequest {
    private Long id;

    private String billCode;

    private int roomMoney;

    private int chiSoDauDien;

    private int chiSoDauNuoc;

    private int chiSoCuoiDien;

    private int chiSoCuoiNuoc;

    private int electricNumber;

    private int waterNumber;

    private int electricMoney;

    private int waterMoney;

    private String payer;

    private Boolean isPay;

    private Date dateCreate;

    private String description;

    private int totalMoney;

    //Phieu thu, phieu chi
    @Enumerated(EnumType.STRING)
    private BillType billType;

    //Tien phong hoac tien thu them
    @Enumerated(EnumType.STRING)
    private BillContent billContent;

}
