package com.fu.lhm.statistic.model;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatistic {

    private int receive;

    private int spend;

    private int revenue;

    private String monthYear;

}
