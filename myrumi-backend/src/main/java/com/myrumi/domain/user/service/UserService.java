package com.myrumi.domain.user.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 사용자 등록
     */
    @Transactional
    public User createUser(User user) {
        // 중복 체크
        if (userRepository.existsByUsername(user.getUsername())) {
            throw CustomException.conflict("이미 존재하는 사용자명입니다.");
        }
        
        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        log.info("Creating new user: {}", user.getUsername());
        return userRepository.save(user);
    }
    
    /**
     * ID로 사용자 조회
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
    }
    
    /**
     * 사용자명으로 조회
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
    }
    
    /**
     * 모든 노인 사용자 조회
     */
    public List<User> findAllElderlyUsers() {
        return userRepository.findByRole(User.UserRole.ELDERLY);
    }
    
    /**
     * 사용자 정보 수정
     */
    @Transactional
    public User updateUser(Long id, User updateData) {
        User user = findById(id);
        
        if (updateData.getName() != null) {
            user.setName(updateData.getName());
        }
        if (updateData.getPhone() != null) {
            user.setPhone(updateData.getPhone());
        }
        if (updateData.getAddress() != null) {
            user.setAddress(updateData.getAddress());
        }
        if (updateData.getProfileImageUrl() != null) {
            user.setProfileImageUrl(updateData.getProfileImageUrl());
        }
        
        log.info("Updated user: {}", user.getUsername());
        return userRepository.save(user);
    }
    
    /**
     * 마지막 로그인 시간 업데이트
     */
    @Transactional
    public void updateLastLogin(Long userId) {
        User user = findById(userId);
        user.updateLastLogin();
        log.info("Updated last login for user: {}", user.getUsername());
    }
    
    /**
     * 사용자 비활성화
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        user.deactivate();
        log.info("Deactivated user: {}", user.getUsername());
    }
    
    /**
     * 비밀번호 검증
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}