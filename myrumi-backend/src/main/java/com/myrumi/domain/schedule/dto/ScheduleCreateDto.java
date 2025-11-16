package com.myrumi.domain.schedule.dto;

import com.myrumi.domain.schedule.entity.Schedule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 일정 생성 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleCreateDto {
    
    @NotBlank(message = "일정 제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
    
    @NotNull(message = "일정 유형은 필수입니다.")
    private Schedule.ScheduleType type;
    
    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Boolean isRecurring;
    
    private Schedule.RecurrencePattern recurrencePattern;
    
    private Integer reminderMinutes;
    
    private Boolean reminderEnabled;
    
    @Size(max = 200, message = "장소는 200자를 초과할 수 없습니다.")
    private String location;
    
    @Size(max = 500, message = "메모는 500자를 초과할 수 없습니다.")
    private String notes;
}