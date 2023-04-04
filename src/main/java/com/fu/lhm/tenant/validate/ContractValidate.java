package com.fu.lhm.tenant.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.financial.repository.BillRepository;
import com.fu.lhm.room.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.tenant.repository.ContractRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ContractValidate {


    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;
    private final BillRepository billRepository;

    public void validateForCreateContract(Long roomId, CreateContractRequest contract) throws ParseException {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));

        LocalDate today = LocalDate.now();

        checkIsRoomHaveContractActive(roomId);

        //validate for contract
        isNotPopulated(contract.getFromDate().toString(),"Vui lòng nhập ngày bắt đầu ký hợp đồng");
        isNotPopulated(contract.getToDate().toString(),"Vui lòng nhập ngày kết thúc hơp đồng");
        isNotPopulated(contract.getDeposit()+"","Vui lòng nhập tiền cọc");
        checkInputToDate(contract.getFromDate(), contract.getToDate(), today);

        //validate for tenant infor
        isNotPopulated(contract.getTenantName(),"Hợp đồng phải có người ký");
        isNotPopulated(contract.getEmail(),"Nhập email người ký hợp đồng");
        isNotPopulated(contract.getPhone(),"Nhập số điện thoại người ký hợp đồng");
        isNotPopulated(contract.getIdentityNumber(),"Nhập số chứng minh nhân dân người ký hợp đồng");
        isNotPopulated(contract.getAddress(),"Nhập địa chỉ người ký hợp đồng");
        validateForValidEmail(contract.getEmail());
        validateForValidPhone(contract.getPhone());
        validateForValidIdentityNumber(contract.getIdentityNumber());

    }

    private void checkIsRoomHaveContractActive(Long roomId){
        List<Contract> listContract = contractRepository.findAllByTenant_Room_Id(roomId);

        boolean isExist = false;

        for(Contract contract : listContract){
            if(contract.isActive()==true){
                isExist=true;
            }
        }
        if(isExist==true){
            throw new BadRequestException("Phòng hiện tại có 1 hợp đồng vẫn còn hiệu lực, không thể tạo hợp đồng thêm");
        }

    }

    private void isNotPopulated(String value, String errorMsg) {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkInputToDate(LocalDate fromDate, LocalDate toDate, LocalDate today){

        if(fromDate.compareTo(toDate)>0){
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày ký");
        }else if(today.compareTo(toDate)>0){
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày hiện tại: " + today);
        }
    }

    public void validateForValidEmail(String email) {
        if (email != null) {
            this.validatorRegexField(email, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", "Email không đúng định dạng!");
        }
    }

    public void validateForValidPhone(String phone) {
        if (phone != null) {
            this.validatorRegexField(phone, "^[0-9]{10}$", "Số điện thoại không đúng định dạng, phải gồm 10 số");
        }
    }

    public void validateForValidIdentityNumber(String CMND) {
        if (CMND != null) {
            this.validatorRegexField(CMND, "^[0-9]{12}$", "Số CMND không đúng định dạng, phải gồm 12 số");
        }
    }

    private void validatorRegexField(String value, String pattern, String errorMsg) throws BadRequestException {
        Pattern pt = Pattern.compile(pattern, 2);
        Matcher matcher = pt.matcher(value);
        if (!matcher.find()) {
            throw new BadRequestException(errorMsg);
        }
    }



}
