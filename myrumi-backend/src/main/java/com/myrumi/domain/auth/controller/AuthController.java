package com.myrumi.domain.auth.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.auth.dto.LoginRequestDto;
import com.myrumi.domain.auth.dto.LoginResponseDto;
import com.myrumi.domain.auth.dto.TokenRefreshRequestDto;
import com.myrumi.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 및 JWT 토큰 발급")
    public ResponseEntity<ResponseDto<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto) {
        
        LoginResponseDto response = authService.login(dto);
        
        return ResponseEntity.ok(ResponseDto.success(response, "로그인 성공"));
    }
    
    /**
     * 토큰 갱신
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token 발급")
    public ResponseEntity<ResponseDto<LoginResponseDto>> refreshToken(
            @Valid @RequestBody TokenRefreshRequestDto dto) {
        
        LoginResponseDto response = authService.refreshToken(dto.getRefreshToken());
        
        return ResponseEntity.ok(ResponseDto.success(response, "토큰 갱신 성공"));
    }
    
    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 (클라이언트에서 토큰 삭제)")
    public ResponseEntity<ResponseDto<Void>> logout(
            @AuthenticationPrincipal String username) {
        
        authService.logout(username);
        
        return ResponseEntity.ok(ResponseDto.success(null, "로그아웃 성공"));
    }
    
    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    @Operation(summary = "내 정보", description = "현재 로그인한 사용자 정보 조회")
    public ResponseEntity<ResponseDto<String>> getCurrentUser(
            @AuthenticationPrincipal String username) {
        
        return ResponseEntity.ok(ResponseDto.success(username, "인증된 사용자입니다."));
    }
}