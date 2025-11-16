package com.myrumi.domain.emotion.dto;

import com.myrumi.domain.emotion.entity.EmotionLog;
import lombok.*;

import java.util.Map;

/**
 * 감정 통계 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionStatisticsDto {
    
    /**
     * 감정 유형별 개수
     */
    private Map<EmotionLog.EmotionType, Long> emotionCounts;
    
    /**
     * 긍정적 감정 비율 (0-100)
     */
    private Double positiveRatio;
    
    /**
     * 부정적 감정 비율 (0-100)
     */
    private Double negativeRatio;
    
    /**
     * 가장 많이 나타난 감정
     */
    private EmotionLog.EmotionType dominantEmotion;
    
    /**
     * 전체 감정 로그 개수
     */
    private Long totalCount;
    
    /**
     * 주의가 필요한 감정 개수
     */
    private Long attentionNeededCount;
}