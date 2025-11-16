package com.myrumi.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 보호자 정보 엔티티
 */
@Entity
@Table(name = "guardians")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guardian {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 보호자 사용자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 담당 노인 (피보호자)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elderly_id", nullable = false)
    private User elderly;
    
    /**
     * 관계 (아들, 딸, 배우자 등)
     */
    @Column(nullable = false, length = 30)
    private String relationship;
    
    /**
     * 우선순위 (1차 보호자, 2차 보호자 등)
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer priority = 1;
    
    /**
     * 비상 연락 가능 여부
     */
    @Column(name = "emergency_contact")
    @Builder.Default
    private Boolean emergencyContact = true;
    
    /**
     * 알림 수신 여부
     */
    @Column(name = "notification_enabled")
    @Builder.Default
    private Boolean notificationEnabled = true;
    
    /**
     * 메모 (특이사항 등)
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 1차 보호자 여부 확인
     */
    public boolean isPrimaryGuardian() {
        return this.priority == 1;
    }
    
    /**
     * 비상 연락 가능 여부 확인
     */
    public boolean canContactForEmergency() {
        return this.emergencyContact && this.notificationEnabled;
    }
}