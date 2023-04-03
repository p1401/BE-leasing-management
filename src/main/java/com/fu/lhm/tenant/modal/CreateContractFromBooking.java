package com.fu.lhm.tenant.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractFromBooking {

    //contract
    private String contractCode;
    private String houseName;
    private String roomName;
    private Date fromDate;
    private Date toDate;
    private boolean isActive;
    private int deposit;

    //tenant

    private Long tenantId;




}
