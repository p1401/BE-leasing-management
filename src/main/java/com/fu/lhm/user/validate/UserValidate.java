package com.fu.lhm.user.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidate {

    private final UserRepository userRepository;

    public void validateCreateUser(RegisterRequest user) throws BadRequestException {

        validateForNameExist(user.getEmail());
        isNotPopulated(user.getEmail(),"Nhập Email");
        isNotPopulated(user.getName(), "Nhập họ tên đầy đủ");
        isNotPopulated(user.getPassword(),"Nhập password");
        isNotPopulated(user.getAddress(),"Nhập địa chỉ");
        isNotPopulated(user.getBirth()+"","Nhập ngày sinh");
        isNotPopulated(user.getIdentityNumber(),"Nhập CMND");
        isNotPopulated(user.getPhone(),"Nhập số điện thoại");

        validateForValidEmail(user.getEmail());
        validateForValidPhone(user.getPhone());
    }

    private void isNotPopulated(String value, String errorMsg) throws BadRequestException {
        if (null == value || value.trim().isEmpty()) {
            throw new BadRequestException(errorMsg);
        }
    }

    private void validateForNameExist(String email) throws BadRequestException {
        if (!userRepository.existsByEmail(email)) {
            return;
        }

        throw new BadRequestException("Email đã được sử dụng!");
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
