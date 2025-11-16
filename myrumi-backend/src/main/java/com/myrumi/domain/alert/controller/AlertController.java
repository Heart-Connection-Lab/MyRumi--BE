package com.myrumi.domain.alert.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.alert.entity.Alert;
import com.myrumi.domain.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
@Tag(name = "Alert", description = "알림 관리 API")
public class AlertController {
    
    private final AlertService alertService;
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 알림 목록", description = "사용자의 알림 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<Page<Alert>>> getUserAlerts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<Alert> alerts = alertService.getUserAlerts(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(ResponseDto.success(alerts));
    }
    
    @GetMapping("/user/{userId}/unread")
    @Operation(summary = "읽지 않은 알림", description = "읽지 않은 알림 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<Alert>>> getUnreadAlerts(@PathVariable Long userId) {
        List<Alert> alerts = alertService.getUnreadAlerts(userId);
        return ResponseEntity.ok(ResponseDto.success(alerts));
    }
    
    @GetMapping("/user/{userId}/unread-count")
    @Operation(summary = "읽지 않은 알림 개수", description = "읽지 않은 알림의 개수를 조회합니다.")
    public ResponseEntity<ResponseDto<Long>> getUnreadCount(@PathVariable Long userId) {
        Long count = alertService.getUnreadCount(userId);
        return ResponseEntity.ok(ResponseDto.success(count));
    }
    
    @PutMapping("/{alertId}/read")
    @Operation(summary = "알림 읽음 처리", description = "알림을 읽음 상태로 변경합니다.")
    public ResponseEntity<ResponseDto<Alert>> markAsRead(@PathVariable Long alertId) {
        Alert alert = alertService.markAsRead(alertId);
        return ResponseEntity.ok(ResponseDto.success(alert));
    }
    
    @PutMapping("/user/{userId}/read-all")
    @Operation(summary = "모든 알림 읽음 처리", description = "사용자의 모든 알림을 읽음 상태로 변경합니다.")
    public ResponseEntity<ResponseDto<Void>> markAllAsRead(@PathVariable Long userId) {
        alertService.markAllAsRead(userId);
        return ResponseEntity.ok(ResponseDto.success(null, "모든 알림이 읽음 처리되었습니다."));
    }
    
    @GetMapping("/user/{userId}/critical")
    @Operation(summary = "긴급 알림 조회", description = "긴급 알림 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<Alert>>> getCriticalAlerts(@PathVariable Long userId) {
        List<Alert> alerts = alertService.getCriticalAlerts(userId);
        return ResponseEntity.ok(ResponseDto.success(alerts));
    }
    
    @DeleteMapping("/{alertId}")
    @Operation(summary = "알림 삭제", description = "알림을 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteAlert(@PathVariable Long alertId) {
        alertService.deleteAlert(alertId);
        return ResponseEntity.ok(ResponseDto.success(null, "알림이 삭제되었습니다."));
    }
}