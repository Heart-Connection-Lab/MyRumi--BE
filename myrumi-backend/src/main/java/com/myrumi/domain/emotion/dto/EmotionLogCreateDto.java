package com.myrumi.domain.emotion.dto;

import com.myrumi.domain.emotion.entity.EmotionLog;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 감정 로그 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLogCreateDto {
    
    @NotNull(message = "감정 유형은 필수입니다.")
    private EmotionLog.EmotionType emotionType;
    
    @NotNull(message = "점수는 필수입니다.")
    @Min(value = 0, message = "점수는 0 이상이어야 합니다.")
    @Max(value = 1, message = "점수는 1 이하여야 합니다.")
    private Double score;
    
    @Size(max = 500, message = "컨텍스트는 500자를 초과할 수 없습니다.")
    private String context;
    
    private Long messageId;
    
    @NotNull(message = "감지 방법은 필수입니다.")
    private EmotionLog.DetectionMethod detectionMethod;
}