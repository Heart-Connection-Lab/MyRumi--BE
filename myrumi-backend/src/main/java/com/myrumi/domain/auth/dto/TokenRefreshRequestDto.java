package com.myrumi.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 토큰 갱신 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshRequestDto {
    
    @NotBlank(message = "Refresh Token은 필수입니다.")
    private String refreshToken;
}