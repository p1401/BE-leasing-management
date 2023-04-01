package com.fu.lhm.room;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendListRoomAndInforRequest {

    private Long roomId;

    private String roomName;

    private int price;

    private int maxTenant;

    private int currenTenant;

    private int area;

    private String currentContract;

    private int moneyNotPay;

    private Date dayEmpty;

    private String tenantBooking;

}
