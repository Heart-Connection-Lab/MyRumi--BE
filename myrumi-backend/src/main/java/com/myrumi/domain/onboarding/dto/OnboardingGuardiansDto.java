package com.myrumi.domain.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

/**
 * 온보딩 - 보호자 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingGuardiansDto {
    
    /**
     * 보호자 목록
     */
    private List<GuardianInfo> guardians;
    
    /**
     * 개별 보호자 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GuardianInfo {
        
        @NotBlank(message = "보호자 이름은 필수입니다.")
        private String name;
        
        @NotBlank(message = "관계는 필수입니다.")
        private String relationship;  // 아들, 딸, 배우자, 며느리, 사위 등
        
        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
                 message = "올바른 전화번호 형식이 아닙니다.")
        private String phone;
        
        /**
         * 우선순위 (1차, 2차 보호자)
         */
        private Integer priority;
        
        /**
         * 비상 연락 가능 여부
         */
        private Boolean emergencyContact;
        
        /**
         * 거주지 (함께 살고 있는지 등)
         */
        private String address;
    }
}