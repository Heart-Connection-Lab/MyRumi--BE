package com.myrumi.domain.schedule.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.alert.entity.Alert;
import com.myrumi.domain.alert.service.AlertService;
import com.myrumi.domain.schedule.entity.Schedule;
import com.myrumi.domain.schedule.repository.ScheduleRepository;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;
    
    /**
     * 일정 생성
     */
    @Transactional
    public Schedule createSchedule(Long userId, Schedule schedule) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        schedule.setUser(user);
        
        log.info("Creating schedule for user {}: {}", userId, schedule.getTitle());
        return scheduleRepository.save(schedule);
    }
    
    /**
     * 일정 조회
     */
    public Schedule getSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> CustomException.notFound("일정을 찾을 수 없습니다."));
    }
    
    /**
     * 사용자의 모든 일정 조회
     */
    public List<Schedule> getUserSchedules(Long userId) {
        return scheduleRepository.findByUserIdOrderByStartTimeAsc(userId);
    }
    
    /**
     * 오늘의 일정 조회
     */
    public List<Schedule> getTodaySchedules(Long userId) {
        return scheduleRepository.findTodaySchedules(userId);
    }
    
    /**
     * 특정 기간의 일정 조회
     */
    public List<Schedule> getSchedulesByPeriod(Long userId, 
                                                LocalDateTime start, 
                                                LocalDateTime end) {
        return scheduleRepository.findByUserIdAndStartTimeBetween(userId, start, end);
    }
    
    /**
     * 일정 유형별 조회
     */
    public List<Schedule> getSchedulesByType(Long userId, Schedule.ScheduleType type) {
        return scheduleRepository.findByUserIdAndType(userId, type);
    }
    
    /**
     * 예정된 일정 조회
     */
    public List<Schedule> getScheduledSchedules(Long userId) {
        return scheduleRepository.findByUserIdAndStatus(userId, Schedule.ScheduleStatus.SCHEDULED);
    }
    
    /**
     * 일정 수정
     */
    @Transactional
    public Schedule updateSchedule(Long scheduleId, Schedule updateData) {
        Schedule schedule = getSchedule(scheduleId);
        
        if (updateData.getTitle() != null) {
            schedule.setTitle(updateData.getTitle());
        }
        if (updateData.getDescription() != null) {
            schedule.setDescription(updateData.getDescription());
        }
        if (updateData.getStartTime() != null) {
            schedule.setStartTime(updateData.getStartTime());
        }
        if (updateData.getEndTime() != null) {
            schedule.setEndTime(updateData.getEndTime());
        }
        if (updateData.getLocation() != null) {
            schedule.setLocation(updateData.getLocation());
        }
        if (updateData.getReminderMinutes() != null) {
            schedule.setReminderMinutes(updateData.getReminderMinutes());
        }
        if (updateData.getNotes() != null) {
            schedule.setNotes(updateData.getNotes());
        }
        
        log.info("Updated schedule {}: {}", scheduleId, schedule.getTitle());
        return scheduleRepository.save(schedule);
    }
    
    /**
     * 일정 완료 처리
     */
    @Transactional
    public Schedule completeSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        schedule.complete();
        
        log.info("Completed schedule {}: {}", scheduleId, schedule.getTitle());
        return scheduleRepository.save(schedule);
    }
    
    /**
     * 일정 취소
     */
    @Transactional
    public Schedule cancelSchedule(Long scheduleId) {
        Schedule schedule = getSchedule(scheduleId);
        schedule.cancel();
        
        log.info("Cancelled schedule {}: {}", scheduleId, schedule.getTitle());
        return scheduleRepository.save(schedule);
    }
    
    /**
     * 일정 삭제
     */
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
        log.info("Deleted schedule {}", scheduleId);
    }
    
    /**
     * 다가오는 일정 알림 (30분 전)
     */
    public List<Schedule> getUpcomingSchedules(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutesLater = now.plusMinutes(30);
        
        return scheduleRepository.findUpcomingSchedules(userId, now, thirtyMinutesLater);
    }
    
    /**
     * 놓친 일정 확인 및 상태 업데이트
     */
    @Transactional
    public void checkAndUpdateMissedSchedules(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        List<Schedule> missedSchedules = scheduleRepository.findMissedSchedules(userId, now);
        
        for (Schedule schedule : missedSchedules) {
            schedule.markAsMissed();
            
            // 놓친 일정 알림
            alertService.createAlert(
                    userId,
                    Alert.AlertType.SCHEDULE_REMINDER,
                    "놓친 일정",
                    String.format("'%s' 일정을 놓치셨습니다.", schedule.getTitle()),
                    Alert.Severity.WARNING
            );
        }
        
        if (!missedSchedules.isEmpty()) {
            log.info("Marked {} schedules as missed for user {}", 
                    missedSchedules.size(), userId);
        }
    }
    
    /**
     * 일정 알림 생성 (예약 시간 기준)
     */
    @Transactional
    public void createScheduleReminder(Schedule schedule) {
        LocalDateTime reminderTime = schedule.getReminderTime();
        
        if (reminderTime != null && LocalDateTime.now().isBefore(reminderTime)) {
            alertService.createAlert(
                    schedule.getUser().getId(),
                    Alert.AlertType.SCHEDULE_REMINDER,
                    "일정 알림",
                    String.format("'%s' 일정이 %d분 후에 시작됩니다.", 
                            schedule.getTitle(), 
                            schedule.getReminderMinutes()),
                    Alert.Severity.INFO
            );
            
            log.info("Created reminder for schedule {}", schedule.getId());
        }
    }
    
    /**
     * 복약 알림 확인 (매시간 실행)
     * 실제로는 스케줄러로 자동 실행되어야 함
     */
    @Scheduled(cron = "0 0 * * * *") // 매시간
    @Transactional
    public void checkMedicationReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        
        // 모든 사용자의 복약 일정 확인 (실제로는 사용자별로 처리)
        // 여기서는 예시로 구현
        log.info("Checking medication reminders at {}", now);
    }
}   