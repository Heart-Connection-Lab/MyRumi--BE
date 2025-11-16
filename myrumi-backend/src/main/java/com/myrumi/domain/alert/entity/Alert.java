package com.myrumi.domain.alert.entity;

import com.myrumi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 알림 엔티티
 */
@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 알림 수신자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 관련 노인 사용자 (보호자/요양보호사에게 발송되는 알림의 경우)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elderly_id")
    private User elderly;
    
    /**
     * 알림 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertType type;
    
    /**
     * 알림 제목
     */
    @Column(nullable = false, length = 100)
    private String title;
    
    /**
     * 알림 내용
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    /**
     * 심각도
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Severity severity = Severity.INFO;
    
    /**
     * 읽음 여부
     */
    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;
    
    /**
     * 읽은 시간
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    /**
     * 관련 엔티티 ID (일정 ID, 대화 ID 등)
     */
    @Column(name = "related_id")
    private Long relatedId;
    
    /**
     * 관련 엔티티 타입
     */
    @Column(name = "related_type", length = 50)
    private String relatedType;
    
    /**
     * 액션 URL (클릭 시 이동할 경로)
     */
    @Column(name = "action_url")
    private String actionUrl;
    
    /**
     * 만료 시간 (null이면 만료 없음)
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 알림 유형
     */
    public enum AlertType {
        EMOTION_DETECTED,       // 부정적 감정 감지
        SCHEDULE_REMINDER,      // 일정 알림
        MEDICATION_REMINDER,    // 복약 알림
        ABNORMAL_ACTIVITY,      // 비정상 활동 감지
        EMERGENCY,              // 긴급 상황
        HEALTH_WARNING,         // 건강 경고
        MESSAGE,                // 메시지 알림
        SYSTEM                  // 시스템 알림
    }
    
    /**
     * 심각도
     */
    public enum Severity {
        INFO,       // 정보
        WARNING,    // 경고
        CRITICAL    // 긴급
    }
    
    /**
     * 알림 읽음 처리
     */
    public void markAsRead() {
        this.isRead = true;
        this.readAt = LocalDateTime.now();
    }
    
    /**
     * 알림이 만료되었는지 확인
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * 긴급 알림인지 확인
     */
    public boolean isCritical() {
        return this.severity == Severity.CRITICAL;
    }
    
    /**
     * 긴급 상황 알림인지 확인
     */
    public boolean isEmergency() {
        return this.type == AlertType.EMERGENCY;
    }
}