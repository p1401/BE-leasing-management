package com.fu.lhm.financial;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity(name = "bills")
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billCode;

    private int electricMoney;

    private int waterMoney;

    private int electricNumber;

    private int waterNumber;

    private String houseName;

    private String roomName;

    private String payer;

    private String billType;

    private String billContent;

    private String description;

    private boolean isPay;

    private Date fromDate;

    private int totalMoney;

}
