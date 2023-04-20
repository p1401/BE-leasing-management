package com.fu.lhm.tenant.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.room.entity.Room;
import com.fu.lhm.room.repository.RoomRepository;
import com.fu.lhm.tenant.entity.Tenant;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
@RequiredArgsConstructor
public class TenantValidate {

    private final RoomRepository roomRepository;

    public void validateForCreateTenant(Long roomId, Tenant tenant) throws BadRequestException {
        checkCurrentNumberTenantInRoom(roomId);


        this.isNotPopulated(tenant.getName(),"Vui lòng nhập họ tên");
        isNotPopulated(tenant.getPhone()+"","Vui lòng nhập số điện thoại để liên hệ");
        isNotPopulated(tenant.getEmail(),"Vui lòng nhập email để liên hệ");
        isNotPopulated(tenant.getAddress(),"Vui lòng nhập địa chỉ");
        isNotPopulated(tenant.getIdentifyNumber()+"", "Vui lòng nhập CMND");
        this.validateForValidEmail(tenant.getEmail());
        this.validateForValidPhone(tenant.getPhone()+"");
        this.isNotPopulated(tenant.getAddress(),"Vui lòng nhập địa chỉ");
    }

    public void validateForUpdateTenant(Tenant tenant) throws BadRequestException {
        this.isNotPopulated(tenant.getName(),"Vui lòng nhập họ tên");
        this.validateForValidEmail(tenant.getEmail());
        this.validateForValidPhone(tenant.getPhone()+"");
        this.isNotPopulated(tenant.getAddress(),"Vui lòng nhập địa chỉ");
    }

    private void checkCurrentNumberTenantInRoom(Long roomId) throws BadRequestException {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("Phòng không tồn tại!"));
        if(room.getMaxTenant()==room.getCurrentTenant()){
            throw new BadRequestException("Phòng đã hết chỗ");
        }
    }

    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty() || value.equalsIgnoreCase("")) {
            throw new BadRequestException(errorMsg);
        }
    }

    public void validateForValidEmail(String email) throws BadRequestException {
        if (email != null) {
            this.validatorRegexField(email, "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", "Email không đúng định dạng!");
        }
    }

    public void validateForValidPhone(String phone) throws BadRequestException {
        if (phone != null) {
            this.validatorRegexField(phone, "^[0-9]{10}$", "Số điện thoại không đúng định dạng!");
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
