package com.elyashevich.network.user;

import com.elyashevich.network.auth.dto.RegisterRequestDTO;
import com.elyashevich.network.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User convert(RegisterRequestDTO dto) {
        return User
                .builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .accountLocked(false)
                .enabled(false)
                .build();
    }
}
