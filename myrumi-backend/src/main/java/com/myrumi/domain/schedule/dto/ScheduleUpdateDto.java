package com.myrumi.domain.schedule.dto;

import com.myrumi.domain.schedule.entity.Schedule;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 일정 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleUpdateDto {
    
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    
    @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
    private String description;
    
    private Schedule.ScheduleType type;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private Integer reminderMinutes;
    
    private Boolean reminderEnabled;
    
    @Size(max = 200, message = "장소는 200자를 초과할 수 없습니다.")
    private String location;
    
    @Size(max = 500, message = "메모는 500자를 초과할 수 없습니다.")
    private String notes;
}