package com.myrumi.domain.onboarding.repository;

import com.myrumi.domain.onboarding.entity.OnboardingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnboardingRepository extends JpaRepository<OnboardingStatus, Long> {
    
    /**
     * 사용자 ID로 온보딩 상태 조회
     */
    Optional<OnboardingStatus> findByUserId(Long userId);
    
    /**
     * 온보딩 완료된 사용자 수 조회
     */
    Long countByIsCompletedTrue();
}