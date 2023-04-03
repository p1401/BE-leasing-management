package com.fu.lhm.tenant.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.tenant.Contract;
import com.fu.lhm.tenant.Tenant;
import com.fu.lhm.tenant.modal.CreateContractRequest;
import com.fu.lhm.tenant.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ContractValidate {

    private final ContractRepository contractRepository;

    public void validateForCreateContract(CreateContractRequest contract) throws ParseException {

        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
        Date fromDate = sdformat.parse(contract.getFromDate());
        Date toDate = sdformat.parse(contract.getToDate());

        isNotPopulated(contract.getTenantName(),"Hợp đồng phải có người ký");
        isNotPopulated(contract.getFromDate().toString(),"Vui lòng nhập ngày bắt đầu ký hợp đồng");
        isNotPopulated(contract.getToDate().toString(),"Vui lòng nhập ngày kết thúc hơp đồng");
        isNotPopulated(contract.getDeposit()+"","Vui lòng nhập Tiền cọc");
        checkInputToDate(fromDate, toDate);
    }

    private void isNotPopulated(String value, String errorMsg) {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkInputToDate(Date fromDate, Date toDate){

        if(fromDate.compareTo(toDate)>0){
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày ký");
        }

    }



}
