package com.myrumi.domain.emotion.dto;

import com.myrumi.domain.emotion.entity.EmotionLog;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 감정 로그 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLogResponseDto {
    
    private Long id;
    private Long userId;
    private EmotionLog.EmotionType emotionType;
    private Double score;
    private String context;
    private Long messageId;
    private EmotionLog.DetectionMethod detectionMethod;
    private LocalDateTime createdAt;
    
    /**
     * Entity를 DTO로 변환
     */
    public static EmotionLogResponseDto from(EmotionLog emotionLog) {
        return EmotionLogResponseDto.builder()
                .id(emotionLog.getId())
                .userId(emotionLog.getUser().getId())
                .emotionType(emotionLog.getEmotionType())
                .score(emotionLog.getScore())
                .context(emotionLog.getContext())
                .messageId(emotionLog.getMessageId())
                .detectionMethod(emotionLog.getDetectionMethod())
                .createdAt(emotionLog.getCreatedAt())
                .build();
    }
}