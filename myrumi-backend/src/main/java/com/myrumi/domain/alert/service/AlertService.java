package com.myrumi.domain.alert.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.alert.entity.Alert;
import com.myrumi.domain.alert.repository.AlertRepository;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    
    /**
     * 알림 생성
     */
    @Transactional
    public Alert createAlert(Long userId, Alert.AlertType type, String title, 
                            String content, Alert.Severity severity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        Alert alert = Alert.builder()
                .user(user)
                .type(type)
                .title(title)
                .content(content)
                .severity(severity)
                .build();
        
        log.info("Creating alert for user {}: {}", userId, type);
        return alertRepository.save(alert);
    }
    
    /**
     * 노인 관련 알림 생성 (보호자/요양보호사에게 발송)
     */
    @Transactional
    public Alert createElderlyAlert(Long recipientId, Long elderlyId, Alert.AlertType type,
                                   String title, String content, Alert.Severity severity) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> CustomException.notFound("수신자를 찾을 수 없습니다."));
        
        User elderly = userRepository.findById(elderlyId)
                .orElseThrow(() -> CustomException.notFound("노인 사용자를 찾을 수 없습니다."));
        
        Alert alert = Alert.builder()
                .user(recipient)
                .elderly(elderly)
                .type(type)
                .title(title)
                .content(content)
                .severity(severity)
                .build();
        
        log.info("Creating elderly-related alert for recipient {}: {}", recipientId, type);
        return alertRepository.save(alert);
    }
    
    /**
     * 사용자 알림 목록 조회
     */
    public Page<Alert> getUserAlerts(Long userId, Pageable pageable) {
        return alertRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
    }
    
    /**
     * 읽지 않은 알림 조회
     */
    public List<Alert> getUnreadAlerts(Long userId) {
        return alertRepository.findByElderly_IdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * 읽지 않은 알림 개수
     */
    public Long getUnreadCount(Long userId) {
        return alertRepository.countByUser_IdAndIsReadFalse(userId);
    }
    
    /**
     * 알림 읽음 처리
     */
    @Transactional
    public Alert markAsRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> CustomException.notFound("알림을 찾을 수 없습니다."));
        
        alert.markAsRead();
        log.info("Marked alert {} as read", alertId);
        return alert;
    }
    
    /**
     * 모든 알림 읽음 처리
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Alert> unreadAlerts = alertRepository.findByUser_IdAndIsReadFalseOrderByCreatedAtDesc(userId);
        unreadAlerts.forEach(Alert::markAsRead);
        log.info("Marked all alerts as read for user {}", userId);
    }
    
    /**
     * 긴급 알림 조회
     */
    public List<Alert> getCriticalAlerts(Long userId) {
        return alertRepository.findByUserIdAndSeverities(
            userId, 
            List.of(Alert.Severity.CRITICAL)
        );
    }
        
    /**
     * 활성 알림 조회 (만료되지 않은 알림)
     */
    public List<Alert> getActiveAlerts(Long userId) {
        return alertRepository.findActiveAlerts(userId, LocalDateTime.now());
    }
    
    /**
     * 알림 삭제
     */
    @Transactional
    public void deleteAlert(Long alertId) {
        alertRepository.deleteById(alertId);
        log.info("Deleted alert {}", alertId);
    }
    
    /**
     * 긴급 상황 알림 생성 (모든 보호자에게)
     */
    @Transactional
    public void createEmergencyAlert(Long elderlyId, String title, String content) {
        User elderly = userRepository.findById(elderlyId)
                .orElseThrow(() -> CustomException.notFound("노인 사용자를 찾을 수 없습니다."));
        log.warn("Emergency alert created for elderly {}: {}", elderlyId, title);
    }
}