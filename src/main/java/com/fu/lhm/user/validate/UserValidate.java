package com.fu.lhm.user.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.jwt.service.JwtService;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.model.UserRequest;
import com.fu.lhm.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;
    private User getUserToken() throws BadRequestException {
        return jwtService.getUser(httpServletRequest);
    }

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

    public void validateChangePassword(String oldpass, String newpass) throws BadRequestException {
        checkOldPassword(oldpass);
        isNotPopulated(oldpass, "Nhập password cũ");
        isNotPopulated(newpass, "Nhập password mới");


    }

    public void checkOldPassword(String password) throws BadRequestException {
        String rawPass = getUserToken().getPassword();
        if(passwordEncoder.matches(password, rawPass)==false){
            throw new BadRequestException("Password cũ không khớp");
        }

    }

    public void validateUpdateUser(UserRequest user) throws BadRequestException {

        isNotPopulated(user.getName(), "Nhập họ tên đầy đủ");
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
