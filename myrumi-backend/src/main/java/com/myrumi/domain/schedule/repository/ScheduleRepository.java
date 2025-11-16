package com.myrumi.domain.schedule.repository;

import com.myrumi.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    /**
     * 사용자의 일정 목록 조회 (시작 시간 순)
     */
    List<Schedule> findByUserIdOrderByStartTimeAsc(Long userId);
    
    /**
     * 사용자의 상태별 일정 조회
     */
    List<Schedule> findByUserIdAndStatus(Long userId, Schedule.ScheduleStatus status);
    
    /**
     * 특정 기간의 일정 조회
     */
    List<Schedule> findByUserIdAndStartTimeBetween(
            Long userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * 일정 유형별 조회
     */
    List<Schedule> findByUserIdAndType(Long userId, Schedule.ScheduleType type);
    
    /**
     * 일정 유형 및 상태별 조회
     */
    List<Schedule> findByUserIdAndTypeAndStatus(
            Long userId, Schedule.ScheduleType type, Schedule.ScheduleStatus status);
    
    /**
     * 오늘의 일정 조회
     */
    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId " +
           "AND DATE(s.startTime) = CURRENT_DATE " +
           "ORDER BY s.startTime ASC")
    List<Schedule> findTodaySchedules(@Param("userId") Long userId);
    
    /**
     * 다가오는 일정 조회 (알림용)
     */
    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId " +
           "AND s.status = 'SCHEDULED' " +
           "AND s.startTime BETWEEN :now AND :future " +
           "ORDER BY s.startTime ASC")
    List<Schedule> findUpcomingSchedules(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now,
            @Param("future") LocalDateTime future);
    
    /**
     * 반복 일정 조회
     */
    List<Schedule> findByUserIdAndIsRecurringTrue(Long userId);
    
    /**
     * 완료되지 않은 과거 일정 조회 (놓친 일정)
     */
    @Query("SELECT s FROM Schedule s WHERE s.user.id = :userId " +
           "AND s.status = 'SCHEDULED' " +
           "AND s.startTime < :now")
    List<Schedule> findMissedSchedules(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now);
}