package com.fu.lhm.tenant.modal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractRequest {

    //contract
    private String contractCode;
    private String houseName;
    private String roomName;
    private String fromDate;
    private String toDate;
    private boolean isActive;
    private int deposit;

    //tenant
    private String tenantName;
    private String email;
    private int phone;
    private String address;

}
