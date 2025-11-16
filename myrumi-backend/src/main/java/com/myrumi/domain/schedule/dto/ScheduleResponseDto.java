package com.myrumi.domain.schedule.dto;

import com.myrumi.domain.schedule.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 일정 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {
    
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Schedule.ScheduleType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isRecurring;
    private Schedule.RecurrencePattern recurrencePattern;
    private Integer reminderMinutes;
    private Boolean reminderEnabled;
    private String location;
    private Schedule.ScheduleStatus status;
    private LocalDateTime completedAt;
    private String notes;
    private LocalDateTime createdAt;
    
    /**
     * Entity를 DTO로 변환
     */
    public static ScheduleResponseDto from(Schedule schedule) {
        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .userId(schedule.getUser().getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .type(schedule.getType())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .isRecurring(schedule.getIsRecurring())
                .recurrencePattern(schedule.getRecurrencePattern())
                .reminderMinutes(schedule.getReminderMinutes())
                .reminderEnabled(schedule.getReminderEnabled())
                .location(schedule.getLocation())
                .status(schedule.getStatus())
                .completedAt(schedule.getCompletedAt())
                .notes(schedule.getNotes())
                .createdAt(schedule.getCreatedAt())
                .build();
    }
}