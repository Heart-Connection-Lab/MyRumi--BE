package com.myrumi.domain.conversation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MessageRole role;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private MessageType messageType;
    
    // 음성 메시지 관련
    @Column(name = "audio_url")
    private String audioUrl;
    
    @Column(name = "audio_duration")
    private Integer audioDuration;
    
    // 감정 분석 결과
    @Column(name = "detected_emotion", length = 20)
    private String detectedEmotion;
    
    @Column(name = "emotion_score")
    private Double emotionScore;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum MessageRole {
        USER,       // 사용자
        ASSISTANT   // AI 비서
    }
    
    public enum MessageType {
        TEXT,       // 텍스트
        VOICE,      // 음성
        IMAGE       // 이미지
    }
}