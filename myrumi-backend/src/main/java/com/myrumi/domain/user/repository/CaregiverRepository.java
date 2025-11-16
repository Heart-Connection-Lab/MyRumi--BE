package com.myrumi.domain.user.repository;

import com.myrumi.domain.user.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 요양보호사 Repository
 */
public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {
    
    /**
     * 특정 노인의 모든 요양보호사 조회
     */
    List<Caregiver> findByElderly_Id(Long elderlyId);
    
    /**
     * 특정 노인의 활동 중인 요양보호사 조회
     */
    List<Caregiver> findByElderly_IdAndWorkStatus(Long elderlyId, Caregiver.WorkStatus workStatus);
    
    /**
     * 특정 사용자가 요양보호사로 등록된 노인 목록
     */
    List<Caregiver> findByUser_Id(Long userId);
}