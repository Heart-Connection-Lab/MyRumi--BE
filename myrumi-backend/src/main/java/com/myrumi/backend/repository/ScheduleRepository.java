package main.java.com.myrumi.backend.repository;

import com.myrumi.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserIdAndCompletedFalseOrderByScheduledAtAsc(Long userId);
    List<Schedule> findByUserIdAndScheduledAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}