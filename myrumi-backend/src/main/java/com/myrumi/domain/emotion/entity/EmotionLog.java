package com.myrumi.domain.emotion.entity;

import com.myrumi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EmotionType emotionType;
    
    @Column(nullable = false)
    private Double score;
    
    @Column(columnDefinition = "TEXT")
    private String context;
    
    @Column(name = "message_id")
    private Long messageId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "detection_method", length = 20)
    private DetectionMethod detectionMethod;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum EmotionType {
        HAPPY,      // 행복
        SAD,        // 슬픔
        ANGRY,      // 화남
        ANXIOUS,    // 불안
        LONELY,     // 외로움
        EXCITED,    // 흥분
        CALM,       // 평온
    }
    
    public enum DetectionMethod {
        TEXT_ANALYSIS,      // 텍스트 분석
        VOICE_ANALYSIS,     // 음성 분석
        SELF_REPORT         // 사용자 직접 입력
    }
    
    // 비즈니스 메서드
    public boolean isNegativeEmotion() {
        return emotionType == EmotionType.SAD || 
               emotionType == EmotionType.ANGRY || 
               emotionType == EmotionType.ANXIOUS || 
               emotionType == EmotionType.LONELY;
    }
    
    public boolean needsAttention() {
        return isNegativeEmotion() && score > 0.7;
    }
}