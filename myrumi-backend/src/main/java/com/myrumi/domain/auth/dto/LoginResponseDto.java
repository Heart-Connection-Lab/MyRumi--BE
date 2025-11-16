package com.myrumi.domain.auth.dto;

import com.myrumi.domain.user.entity.User;
import lombok.*;

/**
 * 로그인 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    
    /**
     * Access Token 
     */
    private String accessToken;
    
    /**
     * Refresh Token
     */
    private String refreshToken;
    
    /**
     * 토큰 타입
     */
    @Builder.Default
    private String tokenType = "Bearer";
    
    /**
     * Access Token 
     */
    private Long expiresIn;
    
    /**
     * 사용자 정보
     */
    private UserInfo userInfo;
    
    /**
     * 사용자 기본 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        private Long id;
        private String username;
        private String name;
        private User.UserRole role;
        private User.UserStatus status;
        
        public static UserInfo from(User user) {
            return UserInfo.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .name(user.getName())
                    .role(user.getRole())
                    .status(user.getStatus())
                    .build();
        }
    }
}