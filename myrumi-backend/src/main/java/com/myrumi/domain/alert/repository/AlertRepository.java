package com.myrumi.domain.alert.repository;

import com.myrumi.domain.alert.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 알림 Repository
 */
public interface AlertRepository extends JpaRepository<Alert, Long> {
    
    /**
     * 특정 사용자의 활성 알림 조회 (만료되지 않은 알림)
     */
    @Query("SELECT a FROM Alert a WHERE a.user.id = :userId AND (a.expiresAt IS NULL OR a.expiresAt > :now)")
    List<Alert> findActiveAlerts(@Param("userId") Long userId, @Param("now") LocalDateTime now);
    
    /**
     * 특정 사용자의 읽지 않은 알림 조회
     */
    @Query("SELECT a FROM Alert a WHERE a.user.id = :userId AND a.isRead = false ORDER BY a.createdAt DESC")
    List<Alert> findUnreadAlerts(@Param("userId") Long userId);
    
    /**
     * 특정 사용자의 모든 알림 조회 (최신순)
     */
    List<Alert> findByUser_IdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 특정 사용자의 모든 알림 조회 (페이징)
     */
    Page<Alert> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * 특정 사용자의 읽지 않은 알림 조회 (Spring Data JPA 메서드명 방식)
     */
    List<Alert> findByUser_IdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
    
    /**
     * 특정 노인의 알림 조회 (보호자/요양보호사용)
     */
    List<Alert> findByElderly_IdOrderByCreatedAtDesc(Long elderlyId);
    
    /**
     * 특정 유형의 알림 조회
     */
    List<Alert> findByUser_IdAndTypeOrderByCreatedAtDesc(Long userId, Alert.AlertType type);
    
    /**
     * 특정 심각도 이상의 알림 조회
     */
    @Query("SELECT a FROM Alert a WHERE a.user.id = :userId AND a.severity IN :severities ORDER BY a.createdAt DESC")
    List<Alert> findByUserIdAndSeverities(@Param("userId") Long userId, @Param("severities") List<Alert.Severity> severities);
    
    /**
     * 사용자의 읽지 않은 알림 개수
     */
    Long countByUser_IdAndIsReadFalse(Long userId);
}