package com.myrumi.domain.user.repository;

import com.myrumi.domain.user.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 보호자 Repository
 */
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    
    /**
     * 특정 노인의 모든 보호자 조회
     */
    List<Guardian> findByElderly_Id(Long elderlyId);
    
    /**
     * 특정 노인의 긴급 연락처 보호자 조회 
     */
    List<Guardian> findByElderly_IdAndEmergencyContactTrueOrderByPriorityAsc(Long elderlyId);
    
    /**
     * 특정 사용자가 보호자로 등록된 노인 목록
     */
    List<Guardian> findByUser_Id(Long userId);
}