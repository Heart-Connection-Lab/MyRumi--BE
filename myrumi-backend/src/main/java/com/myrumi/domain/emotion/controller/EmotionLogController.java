package com.myrumi.domain.emotion.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.emotion.dto.EmotionLogCreateDto;
import com.myrumi.domain.emotion.dto.EmotionLogResponseDto;
import com.myrumi.domain.emotion.dto.EmotionStatisticsDto;
import com.myrumi.domain.emotion.entity.EmotionLog;
import com.myrumi.domain.emotion.service.EmotionLogService;
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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/emotions")
@RequiredArgsConstructor
@Tag(name = "Emotion", description = "감정 로그 관리 API")
public class EmotionLogController {
    
    private final EmotionLogService emotionLogService;
    
    /**
     * 감정 로그 생성
     */
    @PostMapping("/users/{userId}")
    @Operation(summary = "감정 로그 생성", description = "사용자의 감정 상태를 기록합니다.")
    public ResponseEntity<ResponseDto<EmotionLogResponseDto>> createEmotionLog(
            @PathVariable Long userId,
            @Valid @RequestBody EmotionLogCreateDto dto) {
        
        EmotionLog emotionLog;
        
        if (dto.getMessageId() != null) {
            emotionLog = emotionLogService.createEmotionLogWithMessage(
                    userId,
                    dto.getMessageId(),
                    dto.getEmotionType(),
                    dto.getScore(),
                    dto.getContext(),
                    dto.getDetectionMethod()
            );
        } else {
            emotionLog = emotionLogService.createEmotionLog(
                    userId,
                    dto.getEmotionType(),
                    dto.getScore(),
                    dto.getContext(),
                    dto.getDetectionMethod()
            );
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(EmotionLogResponseDto.from(emotionLog)));
    }
    
    /**
     * 사용자 감정 로그 조회
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "감정 로그 조회", description = "사용자의 모든 감정 로그를 조회합니다.")
    public ResponseEntity<ResponseDto<List<EmotionLogResponseDto>>> getUserEmotionLogs(
            @PathVariable Long userId) {
        
        List<EmotionLogResponseDto> logs = emotionLogService.getUserEmotionLogs(userId)
                .stream()
                .map(EmotionLogResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(logs));
    }
    
    /**
     * 최근 감정 로그 조회
     */
    @GetMapping("/users/{userId}/recent")
    @Operation(summary = "최근 감정 로그", description = "최근 10개의 감정 로그를 조회합니다.")
    public ResponseEntity<ResponseDto<List<EmotionLogResponseDto>>> getRecentEmotionLogs(
            @PathVariable Long userId) {
        
        List<EmotionLogResponseDto> logs = emotionLogService.getRecentEmotionLogs(userId)
                .stream()
                .map(EmotionLogResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(logs));
    }
    
    /**
     * 기간별 감정 로그 조회
     */
    @GetMapping("/users/{userId}/period")
    @Operation(summary = "기간별 감정 로그", description = "특정 기간의 감정 로그를 조회합니다.")
    public ResponseEntity<ResponseDto<List<EmotionLogResponseDto>>> getEmotionLogsByPeriod(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<EmotionLogResponseDto> logs = emotionLogService
                .getEmotionLogsByPeriod(userId, start, end)
                .stream()
                .map(EmotionLogResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(logs));
    }
    
    /**
     * 부정적 감정 조회
     */
    @GetMapping("/users/{userId}/negative")
    @Operation(summary = "부정적 감정 조회", description = "주의가 필요한 부정적 감정을 조회합니다.")
    public ResponseEntity<ResponseDto<List<EmotionLogResponseDto>>> getNegativeEmotions(
            @PathVariable Long userId) {
        
        List<EmotionLogResponseDto> logs = emotionLogService.getNegativeEmotions(userId)
                .stream()
                .map(EmotionLogResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(logs));
    }
    
    /**
     * 감정 통계 조회
     */
    @GetMapping("/users/{userId}/statistics")
    @Operation(summary = "감정 통계", description = "특정 기간의 감정 통계를 조회합니다.")
    public ResponseEntity<ResponseDto<Map<EmotionLog.EmotionType, Long>>> getEmotionStatistics(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<EmotionLog.EmotionType, Long> statistics = 
                emotionLogService.getEmotionStatistics(userId, start, end);
        
        return ResponseEntity.ok(ResponseDto.success(statistics));
    }
    
    /**
     * 감정 추세 분석
     */
    @GetMapping("/users/{userId}/trend")
    @Operation(summary = "감정 추세 분석", description = "최근 감정 추세를 분석합니다.")
    public ResponseEntity<ResponseDto<EmotionLogService.EmotionTrend>> analyzeEmotionTrend(
            @PathVariable Long userId) {
        
        EmotionLogService.EmotionTrend trend = emotionLogService.analyzeEmotionTrend(userId);
        
        return ResponseEntity.ok(ResponseDto.success(trend));
    }
}