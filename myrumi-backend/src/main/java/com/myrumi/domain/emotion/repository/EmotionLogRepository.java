package com.myrumi.domain.emotion.repository;

import com.myrumi.domain.emotion.entity.EmotionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmotionLogRepository extends JpaRepository<EmotionLog, Long> {
    
    /**
     * 사용자의 감정 로그 조회 (최신순)
     */
    List<EmotionLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 특정 기간의 감정 로그 조회
     */
    List<EmotionLog> findByUserIdAndCreatedAtBetween(
            Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 특정 감정 유형 조회
     */
    List<EmotionLog> findByUserIdAndEmotionType(
            Long userId, EmotionLog.EmotionType emotionType);
    
    /**
     * 부정적 감정 조회 (주의 필요한 것만)
     */
    @Query("SELECT e FROM EmotionLog e WHERE e.user.id = :userId " +
           "AND e.emotionType IN ('SAD', 'ANGRY', 'ANXIOUS', 'LONELY') " +
           "AND e.score > 0.7 ORDER BY e.createdAt DESC")
    List<EmotionLog> findNegativeEmotionsNeedingAttention(@Param("userId") Long userId);
    
    /**
     * 최근 N개의 감정 로그 조회
     */
    List<EmotionLog> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * 감정 유형별 개수 조회
     */
    Long countByUserIdAndEmotionType(Long userId, EmotionLog.EmotionType emotionType);
    
    /**
     * 특정 메시지의 감정 로그 조회
     */
    List<EmotionLog> findByMessageId(Long messageId);
}