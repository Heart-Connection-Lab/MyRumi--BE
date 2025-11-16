package com.myrumi.domain.user.repository;

import com.myrumi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 사용자명으로 사용자 찾기
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 사용자명 중복 확인
     */
    boolean existsByUsername(String username);
    
    /**
     * 역할별 사용자 목록 조회
     */
    List<User> findByRole(User.UserRole role);
    
    /**
     * 상태별 사용자 목록 조회
     */
    List<User> findByStatus(User.UserStatus status);
    
    /**
     * 전화번호로 사용자 찾기
     */
    Optional<User> findByPhone(String phone);
}