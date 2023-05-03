package com.fu.lhm.contract.model;

import com.fu.lhm.tenant.entity.Tenant;
import lombok.*;

import java.util.Date;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {

    private Long id;
    private String contractCode;
    private Date fromDate;
    private Date toDate;
    private String roomName;
    private String houseName;
    private int floor;
    private int area;
    private Boolean isActive;
    private Long deposit;
    private Integer autoBillDate;
    private Long roomId;
    private String tenantName;

}
