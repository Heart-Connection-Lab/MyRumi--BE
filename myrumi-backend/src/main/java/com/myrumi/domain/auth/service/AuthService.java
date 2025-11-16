package com.myrumi.domain.auth.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.config.jwt.JwtTokenProvider;
import com.myrumi.domain.auth.dto.LoginRequestDto;
import com.myrumi.domain.auth.dto.LoginResponseDto;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Value("${jwt.access-token-validity-in-seconds:3600}")
    private long accessTokenValidity;
    
    /**
     * 로그인
     */
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        // 1. 사용자 조회
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> CustomException.unauthorized("아이디 또는 비밀번호가 일치하지 않습니다."));
        
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw CustomException.unauthorized("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        
        // 3. 계정 상태 확인
        if (user.getStatus() == User.UserStatus.INACTIVE) {
            throw CustomException.forbidden("비활성화된 계정입니다.");
        }
        if (user.getStatus() == User.UserStatus.SUSPENDED) {
            throw CustomException.forbidden("정지된 계정입니다.");
        }
        
        // 4. 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getUsername(), 
                user.getRole().name()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
        
        // 5. 마지막 로그인 시간 업데이트
        user.updateLastLogin();
        
        log.info("User logged in: {}", user.getUsername());
        
        // 6. 응답 생성
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(accessTokenValidity)
                .userInfo(LoginResponseDto.UserInfo.from(user))
                .build();
    }
    
    /**
     * 토큰 갱신
     */
    public LoginResponseDto refreshToken(String refreshToken) {
        // 1. Refresh Token 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw CustomException.unauthorized("유효하지 않은 Refresh Token입니다.");
        }
        
        // 2. 사용자 정보 추출
        String username = jwtTokenProvider.getUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        // 3. 새 Access Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(
                user.getUsername(),
                user.getRole().name()
        );
        
        log.info("Token refreshed for user: {}", username);
        
        return LoginResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)  // Refresh Token은 재사용
                .tokenType("Bearer")
                .expiresIn(accessTokenValidity)
                .userInfo(LoginResponseDto.UserInfo.from(user))
                .build();
    }
    
    /**
     * 로그아웃 (선택적: 토큰 블랙리스트 관리 시 사용)
     */
    @Transactional
    public void logout(String username) {
        // 필요시 Redis에 토큰 블랙리스트 추가
        // 현재는 클라이언트에서 토큰 삭제로 처리
        
        log.info("User logged out: {}", username);
    }
}