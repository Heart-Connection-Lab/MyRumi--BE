package com.myrumi.domain.onboarding.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

/**
 * 온보딩 - 선호도 및 생활패턴
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingPreferencesDto {
    
    /**
     * 관심사 및 취미 (대화 소재용)
     */
    private List<String> interests;
    
    /**
     * 좋아하는 활동
     */
    private List<String> favoriteActivities;
    
    /**
     * 좋아하는 음식
     */
    private List<String> favoriteFoods;
    
    /**
     * 기상 시간
     */
    private LocalTime wakeUpTime;
    
    /**
     * 취침 시간
     */
    private LocalTime bedTime;
    
    /**
     * 아침 식사 시간
     */
    private LocalTime breakfastTime;
    
    /**
     * 점심 식사 시간
     */
    private LocalTime lunchTime;
    
    /**
     * 저녁 식사 시간
     */
    private LocalTime dinnerTime;
    
    /**
     * 선호하는 대화 스타일
     */
    private String communicationStyle;  // 간단한 대화, 상세한 대화 등
    
    /**
     * 종교
     */
    private String religion;
    
    /**
     * 특별히 회상하고 싶은 추억
     */
    private List<String> memorableMoments;
}