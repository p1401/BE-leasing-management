package com.fu.lhm.statistic.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.statistic.model.RevenueStatistic;
import com.fu.lhm.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueStatisticService {

    private final BillRepository billRepository;

    public List<RevenueStatistic> getTotalRevenueStatistic(User user, int year){
        List<Bill> bills = billRepository.findAllByContract_Tenant_Room_House_User(user);
        List<RevenueStatistic> revenueStatisticList = new ArrayList<>();

        LocalDate yearStatistic = LocalDate.of(year, 1, 1);

        for(int i=1;i<=yearStatistic.getMonthValue();i++){
            RevenueStatistic revenueStatistic = new RevenueStatistic();
            int receive=0;
            int spend=0;
            revenueStatistic.setMonthYear(yearStatistic.getMonthValue()+"/"+yearStatistic.getYear());

            for(Bill bill : bills){
                if(bill.getDateCreate().getMonthValue()==yearStatistic.getMonthValue()
                && bill.getDateCreate().getYear()==yearStatistic.getYear()
                && bill.getBillType().equals(BillType.RECEIVE)
                && !bill.getBillContent().equals(BillContent.TIENCOC)){

                    receive=receive+bill.getTotalMoney();

                }else if(bill.getDateCreate().getMonthValue()==yearStatistic.getMonthValue()
                        && bill.getDateCreate().getYear()==yearStatistic.getYear()
                        && bill.getBillType().equals(BillType.SPEND)){

                   spend = spend + bill.getTotalMoney();

                }
            }
            revenueStatistic.setReceive(receive);
            revenueStatistic.setSpend(spend);
            revenueStatistic.setRevenue(receive-spend);

            revenueStatisticList.add(revenueStatistic);
        }

        return revenueStatisticList;
    }

    public List<RevenueStatistic> getHouseRevenueStatistic(long houseId, int year){
        List<Bill> bills = billRepository.findAllByContract_Tenant_Room_House_Id(houseId);
        List<RevenueStatistic> revenueStatisticList = new ArrayList<>();

        LocalDate yearStatistic = LocalDate.of(year, 1, 1);

        for(int i=1;i<=yearStatistic.getMonthValue();i++){
            RevenueStatistic revenueStatistic = new RevenueStatistic();
            int receive=0;
            int spend=0;
            revenueStatistic.setMonthYear(yearStatistic.getMonthValue()+"/"+yearStatistic.getYear());

            for(Bill bill : bills){
                if(bill.getDateCreate().getMonthValue()==yearStatistic.getMonthValue()
                        && bill.getDateCreate().getYear()==yearStatistic.getYear()
                        && bill.getBillType().equals(BillType.RECEIVE)
                        && !bill.getBillContent().equals(BillContent.TIENCOC)){

                    receive=receive+bill.getTotalMoney();

                }else if(bill.getDateCreate().getMonthValue()==yearStatistic.getMonthValue()
                        && bill.getDateCreate().getYear()==yearStatistic.getYear()
                        && bill.getBillType().equals(BillType.SPEND)){

                    spend = spend + bill.getTotalMoney();

                }
            }
            revenueStatistic.setReceive(receive);
            revenueStatistic.setSpend(spend);
            revenueStatistic.setRevenue(receive-spend);

            revenueStatisticList.add(revenueStatistic);
        }

        return revenueStatisticList;
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }





}
