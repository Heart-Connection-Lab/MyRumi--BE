package com.myrumi.domain.schedule.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.schedule.dto.ScheduleCreateDto;
import com.myrumi.domain.schedule.dto.ScheduleResponseDto;
import com.myrumi.domain.schedule.dto.ScheduleUpdateDto;
import com.myrumi.domain.schedule.entity.Schedule;
import com.myrumi.domain.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정 관리 API")
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    /**
     * 일정 생성
     */
    @PostMapping("/users/{userId}")
    @Operation(summary = "일정 생성", description = "새로운 일정을 등록합니다.")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> createSchedule(
            @PathVariable Long userId,
            @Valid @RequestBody ScheduleCreateDto dto) {
        
        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .type(dto.getType())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .isRecurring(dto.getIsRecurring())
                .recurrencePattern(dto.getRecurrencePattern())
                .reminderMinutes(dto.getReminderMinutes())
                .reminderEnabled(dto.getReminderEnabled())
                .location(dto.getLocation())
                .notes(dto.getNotes())
                .build();
        
        Schedule createdSchedule = scheduleService.createSchedule(userId, schedule);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(ScheduleResponseDto.from(createdSchedule)));
    }
    
    /**
     * 일정 조회
     */
    @GetMapping("/{scheduleId}")
    @Operation(summary = "일정 조회", description = "특정 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> getSchedule(
            @PathVariable Long scheduleId) {
        
        Schedule schedule = scheduleService.getSchedule(scheduleId);
        
        return ResponseEntity.ok(ResponseDto.success(ScheduleResponseDto.from(schedule)));
    }
    
    /**
     * 사용자의 모든 일정 조회
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "사용자 일정 목록", description = "사용자의 모든 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getUserSchedules(
            @PathVariable Long userId) {
        
        List<ScheduleResponseDto> schedules = scheduleService.getUserSchedules(userId)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(schedules));
    }
    
    /**
     * 오늘의 일정 조회
     */
    @GetMapping("/users/{userId}/today")
    @Operation(summary = "오늘의 일정", description = "오늘 예정된 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getTodaySchedules(
            @PathVariable Long userId) {
        
        List<ScheduleResponseDto> schedules = scheduleService.getTodaySchedules(userId)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(schedules));
    }
    
    /**
     * 기간별 일정 조회
     */
    @GetMapping("/users/{userId}/period")
    @Operation(summary = "기간별 일정", description = "특정 기간의 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getSchedulesByPeriod(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<ScheduleResponseDto> schedules = scheduleService
                .getSchedulesByPeriod(userId, start, end)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(schedules));
    }
    
    /**
     * 일정 유형별 조회
     */
    @GetMapping("/users/{userId}/type/{type}")
    @Operation(summary = "일정 유형별 조회", description = "특정 유형의 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getSchedulesByType(
            @PathVariable Long userId,
            @PathVariable Schedule.ScheduleType type) {
        
        List<ScheduleResponseDto> schedules = scheduleService.getSchedulesByType(userId, type)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(schedules));
    }
    
    /**
     * 다가오는 일정 조회
     */
    @GetMapping("/users/{userId}/upcoming")
    @Operation(summary = "다가오는 일정", description = "곧 시작될 일정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ScheduleResponseDto>>> getUpcomingSchedules(
            @PathVariable Long userId) {
        
        List<ScheduleResponseDto> schedules = scheduleService.getUpcomingSchedules(userId)
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(schedules));
    }
    
    /**
     * 일정 수정
     */
    @PutMapping("/{scheduleId}")
    @Operation(summary = "일정 수정", description = "일정 정보를 수정합니다.")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody ScheduleUpdateDto dto) {
        
        Schedule updateData = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .type(dto.getType())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .reminderMinutes(dto.getReminderMinutes())
                .reminderEnabled(dto.getReminderEnabled())
                .location(dto.getLocation())
                .notes(dto.getNotes())
                .build();
        
        Schedule updatedSchedule = scheduleService.updateSchedule(scheduleId, updateData);
        
        return ResponseEntity.ok(ResponseDto.success(ScheduleResponseDto.from(updatedSchedule)));
    }
    
    /**
     * 일정 완료 처리
     */
    @PutMapping("/{scheduleId}/complete")
    @Operation(summary = "일정 완료", description = "일정을 완료 상태로 변경합니다.")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> completeSchedule(
            @PathVariable Long scheduleId) {
        
        Schedule schedule = scheduleService.completeSchedule(scheduleId);
        
        return ResponseEntity.ok(ResponseDto.success(ScheduleResponseDto.from(schedule)));
    }
    
    /**
     * 일정 취소
     */
    @PutMapping("/{scheduleId}/cancel")
    @Operation(summary = "일정 취소", description = "일정을 취소합니다.")
    public ResponseEntity<ResponseDto<ScheduleResponseDto>> cancelSchedule(
            @PathVariable Long scheduleId) {
        
        Schedule schedule = scheduleService.cancelSchedule(scheduleId);
        
        return ResponseEntity.ok(ResponseDto.success(ScheduleResponseDto.from(schedule)));
    }
    
    /**
     * 일정 삭제
     */
    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        
        return ResponseEntity.ok(ResponseDto.success(null, "일정이 삭제되었습니다."));
    }
}