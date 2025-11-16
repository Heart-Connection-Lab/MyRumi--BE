package com.myrumi.domain.user.repository; 

import com.myrumi.domain.user.entity.UserDetail;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    
    /**
     * 사용자 ID로 상세 정보 조회
     */
    Optional<UserDetail> findByUser_Id(Long userId);  
    
    /**
     * UserDetail 존재 여부 확인
     */
    boolean existsByUser_Id(Long userId);
}