package com.myrumi.domain.onboarding.entity;

import com.myrumi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 온보딩 진행 상태 엔티티
 */
@Entity
@Table(name = "onboarding_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingStatus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    /**
     * 프로필 단계 완료 여부
     */
    @Column(name = "profile_completed")
    @Builder.Default
    private Boolean profileCompleted = false;
    
    /**
     * 건강 정보 단계 완료 여부
     */
    @Column(name = "health_completed")
    @Builder.Default
    private Boolean healthCompleted = false;
    
    /**
     * 보호자 정보 단계 완료 여부
     */
    @Column(name = "guardians_completed")
    @Builder.Default
    private Boolean guardiansCompleted = false;
    
    /**
     * 선호도 단계 완료 여부
     */
    @Column(name = "preferences_completed")
    @Builder.Default
    private Boolean preferencesCompleted = false;
    
    /**
     * 온보딩 완료 여부
     */
    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;
    
    /**
     * 완료 시간
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 진행률 계산 (0-100)
     */
    public Integer calculateProgress() {
        int completed = 0;
        if (profileCompleted) completed++;
        if (healthCompleted) completed++;
        if (guardiansCompleted) completed++;
        if (preferencesCompleted) completed++;
        
        return (completed * 100) / 4;
    }
    
    /**
     * 모든 단계 완료 확인 및 업데이트
     */
    public void checkAndUpdateCompletion() {
        if (profileCompleted && healthCompleted && 
            guardiansCompleted && preferencesCompleted) {
            this.isCompleted = true;
            this.completedAt = LocalDateTime.now();
        }
    }
}