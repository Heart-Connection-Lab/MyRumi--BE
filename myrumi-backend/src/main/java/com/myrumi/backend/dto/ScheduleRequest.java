package main.java.com.myrumi.backend.dto;

import com.myrumi.backend.entity.ScheduleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleRequest {
    
    private Long userId;
    private String title;
    private String description;
    private ScheduleType type;
    private LocalDateTime scheduledAt;
}