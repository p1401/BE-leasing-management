package com.fu.lhm.tenant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractBookingRequest {

    //contract
    private String contractCode;
    private String houseName;
    private String roomName;
    private Date fromDate;
    private Date toDate;
    private Boolean isActive;
    private Long deposit;

    //tenant

    private Long tenantId;




}
