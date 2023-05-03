package com.fu.lhm.contract.model;

import com.fu.lhm.tenant.entity.Tenant;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Generated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContractRequest {
    //contract
    @NotNull
    private long roomId;
    private Date fromDate;
    private Date toDate;
    private Long deposit;
    private Integer autoBillDate;

    //tenant
    private Tenant tenant;

}
