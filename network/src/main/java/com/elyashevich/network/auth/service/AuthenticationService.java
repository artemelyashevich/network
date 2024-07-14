package com.elyashevich.network.auth.service;

import com.elyashevich.network.auth.dto.RegisterRequestDTO;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegisterRequestDTO dto) throws MessagingException;
}
