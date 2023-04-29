package statistic.service;

import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.bill.service.BillService;
import com.fu.lhm.statistic.model.RevenueStatistic;
import com.fu.lhm.statistic.service.RevenueStatisticService;
import com.fu.lhm.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(MockitoJUnitRunner.class)
public class RevenueStatisticServiceTest {
    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private RevenueStatisticService billService;
    @Test
    public void getRevenueStatistic() {
        User user = new User();
        Long houseId = 1L;
        int year = 2022;

        Bill bill1 = new Bill();
        bill1.setTotalMoney(1000);
        bill1.setBillType(BillType.RECEIVE);
        bill1.setBillContent(BillContent.TIENPHONG);
        bill1.setDateCreate(LocalDate.of(2022, 2, 1));

        Bill bill2 = new Bill();
        bill2.setTotalMoney(500);
        bill2.setBillType(BillType.SPEND);
        bill2.setDateCreate(LocalDate.of(2022, 2, 1));

        List<Bill> bills = Arrays.asList(bill1, bill2);
        when(billRepository.findBills(eq(user.getId()), eq(houseId), eq(null), eq(null), eq(null), eq(null), eq(null), any(Pageable.class)))
                .thenReturn(new PageImpl<>(bills));

        List<RevenueStatistic> revenueStatisticList = billService.getRevenueStatistic(user, houseId, year);

        assertEquals(12, revenueStatisticList.size());

        RevenueStatistic revenueStatistic1 = revenueStatisticList.get(0);
        assertEquals(0, revenueStatistic1.getRevenue());
        assertEquals(Date.from(LocalDate.of(2022, 1, 2).atStartOfDay(ZoneId.systemDefault()).toInstant()), revenueStatistic1.getDate());

        RevenueStatistic revenueStatistic2 = revenueStatisticList.get(1);
        assertEquals(500, revenueStatistic2.getRevenue());
        assertEquals(Date.from(LocalDate.of(2022, 2, 2).atStartOfDay(ZoneId.systemDefault()).toInstant()), revenueStatistic2.getDate());
    }

}