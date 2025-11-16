package com.myrumi.domain.emotion.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.alert.entity.Alert;
import com.myrumi.domain.alert.service.AlertService;
import com.myrumi.domain.emotion.entity.EmotionLog;
import com.myrumi.domain.emotion.repository.EmotionLogRepository;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmotionLogService {
    
    private final EmotionLogRepository emotionLogRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;
    
    /**
     * 감정 로그 생성
     */
    @Transactional
    public EmotionLog createEmotionLog(Long userId, EmotionLog.EmotionType emotionType,
                                       Double score, String context,
                                       EmotionLog.DetectionMethod detectionMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        EmotionLog emotionLog = EmotionLog.builder()
                .user(user)
                .emotionType(emotionType)
                .score(score)
                .context(context)
                .detectionMethod(detectionMethod)
                .build();
        
        EmotionLog savedLog = emotionLogRepository.save(emotionLog);
        
        // 주의가 필요한 감정인 경우 알림 생성
        if (savedLog.needsAttention()) {
            createEmotionAlert(userId, emotionType, score);
        }
        
        log.info("Created emotion log for user {}: {} (score: {})", 
                userId, emotionType, score);
        
        return savedLog;
    }
    
    /**
     * 메시지와 연결된 감정 로그 생성
     */
    @Transactional
    public EmotionLog createEmotionLogWithMessage(Long userId, Long messageId,
                                                  EmotionLog.EmotionType emotionType,
                                                  Double score, String context,
                                                  EmotionLog.DetectionMethod detectionMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        EmotionLog emotionLog = EmotionLog.builder()
                .user(user)
                .messageId(messageId)
                .emotionType(emotionType)
                .score(score)
                .context(context)
                .detectionMethod(detectionMethod)
                .build();
        
        EmotionLog savedLog = emotionLogRepository.save(emotionLog);
        
        if (savedLog.needsAttention()) {
            createEmotionAlert(userId, emotionType, score);
        }
        
        log.info("Created emotion log with message {} for user {}: {}", 
                messageId, userId, emotionType);
        
        return savedLog;
    }
    
    /**
     * 사용자의 감정 로그 조회
     */
    public List<EmotionLog> getUserEmotionLogs(Long userId) {
        return emotionLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * 최근 감정 로그 조회 (10개)
     */
    public List<EmotionLog> getRecentEmotionLogs(Long userId) {
        return emotionLogRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId);
    }
    
    /**
     * 특정 기간의 감정 로그 조회
     */
    public List<EmotionLog> getEmotionLogsByPeriod(Long userId, 
                                                    LocalDateTime start, 
                                                    LocalDateTime end) {
        return emotionLogRepository.findByUserIdAndCreatedAtBetween(userId, start, end);
    }
    
    /**
     * 주의가 필요한 부정적 감정 조회
     */
    public List<EmotionLog> getNegativeEmotions(Long userId) {
        return emotionLogRepository.findNegativeEmotionsNeedingAttention(userId);
    }
    
    /**
     * 감정 통계 조회
     */
    public Map<EmotionLog.EmotionType, Long> getEmotionStatistics(Long userId, 
                                                                   LocalDateTime start,
                                                                   LocalDateTime end) {
        List<EmotionLog> logs = emotionLogRepository
                .findByUserIdAndCreatedAtBetween(userId, start, end);
        
        return logs.stream()
                .collect(Collectors.groupingBy(
                        EmotionLog::getEmotionType, 
                        Collectors.counting()
                ));
    }
    
    /**
     * 평균 감정 점수 계산
     */
    public Double getAverageEmotionScore(Long userId, 
                                        EmotionLog.EmotionType emotionType,
                                        LocalDateTime start,
                                        LocalDateTime end) {
        List<EmotionLog> logs = emotionLogRepository
                .findByUserIdAndCreatedAtBetween(userId, start, end);
        
        return logs.stream()
                .filter(log -> log.getEmotionType() == emotionType)
                .mapToDouble(EmotionLog::getScore)
                .average()
                .orElse(0.0);
    }
    
    /**
     * 감정 추세 분석 (최근 7일 vs 이전 7일)
     */
    public EmotionTrend analyzeEmotionTrend(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusDays(7);
        LocalDateTime twoWeeksAgo = now.minusDays(14);
        
        // 최근 7일
        List<EmotionLog> recentLogs = emotionLogRepository
                .findByUserIdAndCreatedAtBetween(userId, weekAgo, now);
        
        // 이전 7일
        List<EmotionLog> previousLogs = emotionLogRepository
                .findByUserIdAndCreatedAtBetween(userId, twoWeeksAgo, weekAgo);
        
        long recentNegativeCount = recentLogs.stream()
                .filter(EmotionLog::isNegativeEmotion)
                .count();
        
        long previousNegativeCount = previousLogs.stream()
                .filter(EmotionLog::isNegativeEmotion)
                .count();
        
        return EmotionTrend.builder()
                .recentNegativeCount(recentNegativeCount)
                .previousNegativeCount(previousNegativeCount)
                .isImproving(recentNegativeCount < previousNegativeCount)
                .build();
    }
    
    /**
     * 감정 기반 알림 생성
     */
    private void createEmotionAlert(Long userId, EmotionLog.EmotionType emotionType, Double score) {
        String title = "감정 상태 알림";
        String content = String.format("%s 감정이 감지되었습니다. (강도: %.1f)", 
                getEmotionKoreanName(emotionType), score * 100);
        
        alertService.createAlert(
                userId,
                Alert.AlertType.EMOTION_DETECTED,
                title,
                content,
                Alert.Severity.WARNING
        );
        
        log.info("Created emotion alert for user {}", userId);
    }
    
    /**
     * 감정 한글명 반환
     */
    private String getEmotionKoreanName(EmotionLog.EmotionType emotionType) {
        switch (emotionType) {
            case HAPPY: return "행복";
            case SAD: return "슬픔";
            case ANGRY: return "화남";
            case ANXIOUS: return "불안";
            case LONELY: return "외로움";
            case EXCITED: return "흥분";
            case CALM: return "평온";
            default: return emotionType.toString();
        }
    }
    
    /**
     * 감정 추세 DTO
     */
    @lombok.Data
    @lombok.Builder
    public static class EmotionTrend {
        private Long recentNegativeCount;
        private Long previousNegativeCount;
        private Boolean isImproving;
    }
}