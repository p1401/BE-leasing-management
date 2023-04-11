package com.fu.lhm.contract.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.contract.entity.Contract;
import com.fu.lhm.contract.model.ContractRequest;
import com.fu.lhm.contract.repository.ContractRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ContractValidate {


    private final RoomRepository roomRepository;
    private final ContractRepository contractRepository;

    public void validateForCreateContract(ContractRequest contract) throws BadRequestException {

        long roomId = contract.getRoomId();

        roomRepository.findById(roomId).orElseThrow(() -> new BadRequestException("Phòng không tồn tại!"));

        validateRoomHaveContractActive(roomId);

        Date fromDate = contract.getFromDate();
        Date toDate = contract.getToDate();

        isNotPopulated(contract.getTenant().getName(), "Hợp đồng phải có người ký");
        isNotPopulated(contract.getFromDate().toString(), "Vui lòng nhập ngày bắt đầu ký hợp đồng");
        isNotPopulated(contract.getToDate().toString(), "Vui lòng nhập ngày kết thúc hơp đồng");
        isNotPopulated(String.valueOf(contract.getDeposit()), "Vui lòng nhập Tiền cọc");
        checkInputToDate(fromDate, toDate);
    }

    private void validateRoomHaveContractActive(Long roomId) throws BadRequestException {
        List<Contract> listContract = contractRepository.findAllByTenant_Room_Id(roomId);

        for (Contract contract : listContract) {
            if (contract.getIsActive()) {
                throw new BadRequestException("Phòng hiện tại có 1 hợp đồng vẫn còn hiệu lực, không thể tạo hợp đồng thêm");
            }
        }
    }

    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void checkInputToDate(Date fromDate, Date toDate) throws BadRequestException {

        if (fromDate.compareTo(toDate) > 0) {
            throw new BadRequestException("Ngày kết thúc phải lớn hơn ngày ký");
        }

    }


}
