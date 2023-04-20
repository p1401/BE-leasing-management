package com.fu.lhm.user.validate;

import com.fu.lhm.exception.BadRequestException;
import com.fu.lhm.house.entity.House;
import com.fu.lhm.user.entity.User;
import com.fu.lhm.user.model.RegisterRequest;
import com.fu.lhm.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidate {

    private final UserRepository userRepository;

    public void validateCreateUser(RegisterRequest user) throws BadRequestException {

        validateForNameExist(user.getEmail());

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

}
