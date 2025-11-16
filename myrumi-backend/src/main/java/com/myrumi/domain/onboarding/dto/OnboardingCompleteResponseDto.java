package com.myrumi.domain.onboarding.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 온보딩 완료 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingCompleteResponseDto {
    
    /**
     * 사용자 ID
     */
    private Long userId;
    
    /**
     * 온보딩 완료 여부
     */
    private Boolean completed;
    
    /**
     * 온보딩 진행률 (0-100)
     */
    private Integer progress;
    
    /**
     * 완료된 단계
     */
    private OnboardingSteps completedSteps;
    
    /**
     * 완료 시간
     */
    private LocalDateTime completedAt;
    
    /**
     * 환영 메시지
     */
    private String welcomeMessage;
    
    /**
     * 온보딩 단계별 완료 상태
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OnboardingSteps {
        private Boolean profile;
        private Boolean health;
        private Boolean guardians;
        private Boolean preferences;
    }
}