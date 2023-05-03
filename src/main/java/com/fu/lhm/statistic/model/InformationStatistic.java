package com.fu.lhm.statistic.model;


import lombok.*;

@Generated
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InformationStatistic {

    private int numberHouse;

    private int numberEmptyRoom;

    private int numberRoom;

    private int numberContractExpired30days;

    private int numberTenant;
}
