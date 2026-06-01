package com.salon.appointment.service;

import com.salon.appointment.dto.request.LoginRequest;
import com.salon.appointment.dto.request.RefreshTokenRequest;
import com.salon.appointment.dto.request.RegisterRequest;
import com.salon.appointment.dto.response.AuthResponse;
import com.salon.appointment.entity.RefreshToken;
import com.salon.appointment.entity.User;
import com.salon.appointment.enums.Role;
import com.salon.appointment.exception.AppException;
import com.salon.appointment.repository.RefreshTokenRepository;
import com.salon.appointment.repository.UserRepository;
import com.salon.appointment.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new AppException("Email already registered", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        userRepository.save(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refershToken = createRefreshToken(user);

        return buildAuthResponse(accessToken, refershToken, user);
    }


    private String createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    private AuthResponse buildAuthResponse(String accessToken, String refreshToken, User user){
        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer ",
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = createRefreshToken(user);

        return buildAuthResponse(accessToken, refreshToken, user);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException("Invalid refresh token", HttpStatus.UNAUTHORIZED));

        if(refreshToken.isExpired()){
            throw new AppException("Refresh token is expired", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.save(refreshToken);
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = createRefreshToken(user);

        return buildAuthResponse(newAccessToken, newRefreshToken, user);
    }

    @Transactional
    public void logout(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new AppException("Invalid authorization header", HttpStatus.BAD_REQUEST);
        }
        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));


    }
}
