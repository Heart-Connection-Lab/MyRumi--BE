package com.myrumi.domain.schedule.entity;

import com.myrumi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 일정 엔티티
 */
@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 일정 소유자 (노인)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * 일정 제목
     */
    @Column(nullable = false, length = 100)
    private String title;
    
    /**
     * 일정 설명
     */
    @Column(columnDefinition = "TEXT")
    private String description;
    
    /**
     * 일정 유형
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ScheduleType type;
    
    /**
     * 시작 시간
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    /**
     * 종료 시간
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    /**
     * 반복 여부
     */
    @Column(name = "is_recurring")
    @Builder.Default
    private Boolean isRecurring = false;
    
    /**
     * 반복 패턴 (DAILY, WEEKLY, MONTHLY 등)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_pattern", length = 20)
    private RecurrencePattern recurrencePattern;
    
    /**
     * 알림 시간 (분 단위, 일정 시작 전)
     */
    @Column(name = "reminder_minutes")
    private Integer reminderMinutes;
    
    /**
     * 알림 활성화 여부
     */
    @Column(name = "reminder_enabled")
    @Builder.Default
    private Boolean reminderEnabled = true;
    
    /**
     * 장소
     */
    @Column(length = 200)
    private String location;
    
    /**
     * 일정 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ScheduleStatus status = ScheduleStatus.SCHEDULED;
    
    /**
     * 완료 시간
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    /**
     * 메모
     */
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 일정 유형
     */
    public enum ScheduleType {
        MEDICATION,     // 복약
        HOSPITAL,       // 병원 방문
        EXERCISE,       // 운동
        MEAL,           // 식사
        ACTIVITY,       // 여가 활동
        WELFARE,        // 복지관 프로그램
        MEETING,        // 모임
        OTHER           // 기타
    }
    
    /**
     * 반복 패턴
     */
    public enum RecurrencePattern {
        DAILY,          // 매일
        WEEKLY,         // 매주
        MONTHLY,        // 매월
        CUSTOM          // 커스텀
    }
    
    /**
     * 일정 상태
     */
    public enum ScheduleStatus {
        SCHEDULED,      // 예정됨
        COMPLETED,      // 완료
        CANCELLED,      // 취소됨
        MISSED          // 놓침
    }
    
    /**
     * 일정 완료 처리
     */
    public void complete() {
        this.status = ScheduleStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    
    /**
     * 일정 취소
     */
    public void cancel() {
        this.status = ScheduleStatus.CANCELLED;
    }
    
    /**
     * 놓친 일정으로 표시
     */
    public void markAsMissed() {
        this.status = ScheduleStatus.MISSED;
    }
    
    /**
     * 알림 시간 계산
     */
    public LocalDateTime getReminderTime() {
        if (reminderMinutes != null && reminderEnabled) {
            return startTime.minusMinutes(reminderMinutes);
        }
        return null;
    }
    
    /**
     * 일정이 진행 중인지 확인
     */
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && 
               (endTime == null || now.isBefore(endTime)) &&
               status == ScheduleStatus.SCHEDULED;
    }
    
    /**
     * 일정이 곧 시작되는지 확인 (30분 이내)
     */
    public boolean isUpcoming() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
        return startTime.isAfter(now) && 
               startTime.isBefore(thirtyMinutesLater) &&
               status == ScheduleStatus.SCHEDULED;
    }
}