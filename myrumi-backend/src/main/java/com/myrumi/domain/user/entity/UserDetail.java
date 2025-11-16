package com.myrumi.domain.user.entity; 
import com.myrumi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 사용자 상세 정보 (온보딩 데이터)
 */
@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    // ===== 건강 정보 =====
    @Column(name = "chronic_diseases", columnDefinition = "TEXT")
    private String chronicDiseases;
    
    @Column(name = "medications", columnDefinition = "TEXT")
    private String medications;
    
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    @Column(name = "cognitive_status", length = 50)
    private String cognitiveStatus;
    
    @Column(name = "mobility_status", length = 50)
    private String mobilityStatus;
    
    @Column(name = "health_notes", columnDefinition = "TEXT")
    private String healthNotes;
    
    // ===== 선호도 및 생활패턴 =====
    @Column(name = "interests", columnDefinition = "TEXT")
    private String interests;
    
    @Column(name = "favorite_activities", columnDefinition = "TEXT")
    private String favoriteActivities;
    
    @Column(name = "favorite_foods", columnDefinition = "TEXT")
    private String favoriteFoods;
    
    @Column(name = "wake_up_time")
    private LocalTime wakeUpTime;
    
    @Column(name = "bed_time")
    private LocalTime bedTime;
    
    @Column(name = "breakfast_time")
    private LocalTime breakfastTime;
    
    @Column(name = "lunch_time")
    private LocalTime lunchTime;
    
    @Column(name = "dinner_time")
    private LocalTime dinnerTime;
    
    @Column(name = "communication_style", length = 50)
    private String communicationStyle;
    
    @Column(length = 50)
    private String religion;
    
    @Column(name = "memorable_moments", columnDefinition = "TEXT")
    private String memorableMoments;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}