package com.elyashevich.network.auth.service;

import com.elyashevich.network.auth.dto.RegisterRequestDTO;
import com.elyashevich.network.email.EmailTemplateName;
import com.elyashevich.network.email.service.EmailService;
import com.elyashevich.network.role.Role;
import com.elyashevich.network.role.RoleRepository;
import com.elyashevich.network.user.UserMapper;
import com.elyashevich.network.user.entity.Token;
import com.elyashevich.network.user.entity.User;
import com.elyashevich.network.user.repository.TokenRepository;
import com.elyashevich.network.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @Value("${application.mail.frontend.activation-url}")
    private String activationUrl;

    public void register(RegisterRequestDTO dto) throws MessagingException {
        final Role userRole = this.roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("ROLE USER was not initialized"));
        final User user = this.userMapper.convert(dto);
        user.setRoles(List.of(userRole));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        this.userRepository.save(user);
        this.sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        final String newToken = this.generateAndSaveActivationToken(user);
        this.emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                this.activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        final String generatedToken = this.generateActivationToken(6);
        final Token token = Token
                .builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();
        this.tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        String characters = "0123456789";
        StringBuilder tokenBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            tokenBuilder.append(characters.charAt(randomIndex));
        }
        return tokenBuilder.toString();
    }
}
