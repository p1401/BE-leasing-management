package com.fu.lhm.bill.validate;

import com.fu.lhm.bill.entity.BillContent;
import com.fu.lhm.bill.entity.BillType;
import com.fu.lhm.bill.modal.BillReceiveRequest;
import com.fu.lhm.bill.modal.BillSpendRequest;
import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.bill.entity.Bill;
import com.fu.lhm.bill.repository.BillRepository;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.repository.ContractRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BillValidate {

    private final BillRepository billRepository;

    private final ContractRepository contractRepository;

    private final RoomRepository roomRepository;

    public void validateForCreateBillTienPhong(Long roomId, BillReceiveRequest bill) throws BadRequestException {

        roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));

        Contract contract = contractRepository.findByTenant_Room_IdAndIsActiveTrue(roomId);
        LocalDate dateCreate = convertToLocalDateViaInstant(bill.getDateCreate());

        checkBillExistsInMonthAndYear(roomId, dateCreate.getMonth().getValue(), dateCreate.getYear());

        isNotPopulated(bill.getRoomMoney() + "", "Nhập tiền phòng");
        isNotPopulated(bill.getChiSoDauDien() + "", "Nhập chỉ số đầu điện");
        isNotPopulated(bill.getChiSoDauNuoc() + "", "Nhập chỉ số đầu nước");
        isNotPopulated(bill.getChiSoCuoiDien() + "", "Nhập chỉ số cuối điện");
        isNotPopulated(bill.getChiSoCuoiNuoc() + "", "Nhập chỉ số cuối nuóc");
        isNotPopulated(bill.getWaterNumber() + "", "Nhập số lượng nước");
        isNotPopulated(bill.getElectricNumber() + "", "Nhập số lượng điện");

        isNotPopulated(bill.getBillContent().name(), "Nhập nội dung hóa đơn");
        isNotPopulated(bill.getBillType().name(), "Nhập kiểu hóa đơn");
        isNotPopulated(bill.getIsPay() + "", "Tích đã nộp hay chưa nộp");
        isNotPopulated(bill.getDateCreate() + "", "Nhập ngày tạo");
        isNotPopulated(bill.getDescription(), "Nhập mô tả");
        isNotPopulated(bill.getTotalMoney() + "", "Nhập tổng tiền");

        validateForChiSoDien(bill.getChiSoDauDien(), bill.getChiSoCuoiDien());
        validateForChiSoNuoc(bill.getChiSoDauNuoc(), bill.getChiSoCuoiNuoc());
        validateForNumberElectric(bill.getElectricNumber());
        validateForNumberWater(bill.getWaterNumber());
        validateForTotalMoney(bill.getTotalMoney());
    }

    public void validateForCreateBillTienPhuTroi(BillReceiveRequest bill) throws BadRequestException {

        isNotPopulated(bill.getRoomMoney() + "", "Nhập tiền phòng");
        isNotPopulated(bill.getChiSoDauDien() + "", "Nhập chỉ số đầu điện");
        isNotPopulated(bill.getChiSoDauNuoc() + "", "Nhập chỉ số đầu nước");
        isNotPopulated(bill.getChiSoCuoiDien() + "", "Nhập chỉ số cuối điện");
        isNotPopulated(bill.getChiSoCuoiNuoc() + "", "Nhập chỉ số cuối nuóc");
        isNotPopulated(bill.getWaterNumber() + "", "Nhập số lượng nước");
        isNotPopulated(bill.getElectricNumber() + "", "Nhập số lượng điện");

        isNotPopulated(bill.getBillContent().name(), "Nhập nội dung hóa đơn");
        isNotPopulated(bill.getBillType().name(), "Nhập kiểu hóa đơn");
        isNotPopulated(bill.getIsPay() + "", "Tích đã nộp hay chưa nộp");
        isNotPopulated(bill.getDateCreate() + "", "Nhập ngày tạo");
        isNotPopulated(bill.getDescription(), "Nhập mô tả");
        isNotPopulated(bill.getTotalMoney() + "", "Nhập tổng tiền");

        validateForChiSoDien(bill.getChiSoDauDien(), bill.getChiSoCuoiDien());
        validateForChiSoNuoc(bill.getChiSoDauNuoc(), bill.getChiSoCuoiNuoc());
        validateForNumberElectric(bill.getElectricNumber());
        validateForNumberWater(bill.getWaterNumber());
        validateForTotalMoney(bill.getTotalMoney());
    }

    public void validateforCreateBillSpend(BillSpendRequest bill) throws BadRequestException {
        isNotPopulated(bill.getBillType().name(), "Nhập kiểu hóa đơn");
        isNotPopulated(bill.getDateCreate() + "", "Nhập ngày tạo");
        isNotPopulated(bill.getDescription(), "Nhập mô tả");
        isNotPopulated(bill.getTotalMoney() + "", "Nhập tổng tiền");
    }

    public void checkBillExistsInMonthAndYear( Long roomId, int month, int year) throws BadRequestException {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Bill> listBill = billRepository.findByContract_Tenant_Room_IdAndBillTypeAndBillContentAndDateCreateBetween(roomId, BillType.RECEIVE, BillContent.TIENPHONG, startDate, endDate);
        if (listBill.isEmpty()) {
        } else {
            throw new BadRequestException("Có " + listBill.size() + " hóa đơn tiền phòng đã được tạo trong tháng " + month + "/" + year);
        }
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    public void validateForChiSoDien(int chiSoDauDien, int chiSoCuoiDien) throws BadRequestException {
        if (chiSoDauDien > chiSoCuoiDien) {
            throw new BadRequestException("Chỉ số đầu của điện phải nhỏ hơn chỉ số cuối");
        } else if (chiSoDauDien < 0 || chiSoCuoiDien < 0) {
            throw new BadRequestException("Chỉ số đầu và cuối của điện phải >=0");
        }
    }

    public void validateForChiSoNuoc(int chiSoDauNuoc, int chiSoCuoiNuoc) throws BadRequestException {
        if (chiSoDauNuoc > chiSoCuoiNuoc) {
            throw new BadRequestException("Chỉ số đầu của nước phải nhỏ hơn chỉ số cuối");
        } else if (chiSoDauNuoc < 0 || chiSoCuoiNuoc < 0) {
            throw new BadRequestException("Chỉ số đầu và cuối của nước phải >=0");
        }
    }

    public void validateForNumberElectric(int numberElectrict) throws BadRequestException {
        if (numberElectrict < 0) {
            throw new BadRequestException("Lượng điện sử dụng phải >=0");
        }
    }

    public void validateForNumberWater(int numberWater) throws BadRequestException {
        if (numberWater < 0) {
            throw new BadRequestException("Lượng nước sử dụng phải >=0");
        }
    }

    public void validateForTotalMoney(int totalMoney) throws BadRequestException {
        if (totalMoney <= 0) {
            throw new BadRequestException("Tổng tiền phải >0");
        }
    }


}
