package com.myrumi.domain.onboarding.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * 온보딩 - 건강 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingHealthDto {
    
    /**
     * 만성질환 목록 (고혈압, 당뇨, 관절염 등)
     */
    private List<String> chronicDiseases;
    
    /**
     * 복용 중인 약물 정보
     */
    private List<MedicationInfo> medications;
    
    /**
     * 알레르기 정보
     */
    private List<String> allergies;
    
    /**
     * 인지 상태 (정상, 경증 치매 등)
     */
    private String cognitiveStatus;
    
    /**
     * 거동 능력 (자유로움, 보조 필요, 휠체어 등)
     */
    private String mobilityStatus;
    
    /**
     * 특이사항 메모
     */
    @Size(max = 500, message = "특이사항은 500자를 초과할 수 없습니다.")
    private String notes;
    
    /**
     * 복약 정보
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicationInfo {
        private String name;            // 약 이름
        private String dosage;          // 복용량
        private String frequency;       // 복용 빈도 (아침, 점심, 저녁 등)
        private String purpose;         // 복용 목적
    }
}