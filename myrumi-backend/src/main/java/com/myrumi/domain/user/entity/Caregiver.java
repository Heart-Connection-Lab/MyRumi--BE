package com.myrumi.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 요양보호사 정보 엔티티
 */
@Entity
@Table(name = "caregivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caregiver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 요양보호사 사용자 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 담당 노인
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elderly_id", nullable = false)
    private User elderly;
    
    /**
     * 자격증 번호
     */
    @Column(name = "license_number", length = 50)
    private String licenseNumber;
    
    /**
     * 자격증 발급일
     */
    @Column(name = "license_issue_date")
    private LocalDate licenseIssueDate;
    
    /**
     * 소속 기관 (복지관, 요양센터 등)
     */
    @Column(length = 100)
    private String organization;
    
    /**
     * 근무 시작일
     */
    @Column(name = "work_start_date")
    private LocalDate workStartDate;
    
    /**
     * 근무 종료일 (null이면 현재 근무 중)
     */
    @Column(name = "work_end_date")
    private LocalDate workEndDate;
    
    /**
     * 근무 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private WorkStatus workStatus = WorkStatus.ACTIVE;
    
    /**
     * 전문 분야 (치매 케어, 신체 케어 등)
     */
    @Column(length = 100)
    private String specialization;
    
    /**
     * 메모
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum WorkStatus {
        ACTIVE,     // 근무 중
        INACTIVE,   // 휴직
        COMPLETED   // 근무 종료
    }
    
    /**
     * 현재 근무 중인지 확인
     */
    public boolean isCurrentlyWorking() {
        return this.workStatus == WorkStatus.ACTIVE && 
               (this.workEndDate == null || this.workEndDate.isAfter(LocalDate.now()));
    }
    
    /**
     * 근무 종료 처리
     */
    public void completeWork(LocalDate endDate) {
        this.workEndDate = endDate;
        this.workStatus = WorkStatus.COMPLETED;
    }
}