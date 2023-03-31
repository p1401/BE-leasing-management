package com.fu.lhm.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date fromDate;
    private Date toDate;
    private boolean isActive;
    private int deposit;

    //tenant

    private String tenantName;
    private String Email;
    private int Phone;
    private String Address;

}
