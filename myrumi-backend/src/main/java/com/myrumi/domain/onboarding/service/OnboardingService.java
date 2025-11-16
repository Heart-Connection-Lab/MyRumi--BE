package com.myrumi.domain.onboarding.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.onboarding.dto.*;
import com.myrumi.domain.onboarding.entity.OnboardingStatus;
import com.myrumi.domain.user.entity.UserDetail;
import com.myrumi.domain.onboarding.repository.OnboardingRepository;
import com.myrumi.domain.user.repository.UserDetailRepository;
import com.myrumi.domain.user.entity.Guardian;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.GuardianRepository;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OnboardingService {
    
    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final UserDetailRepository userDetailRepository;
    private final GuardianRepository guardianRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * 온보딩 시작 - 온보딩 상태 생성
     */
    @Transactional
    public OnboardingStatus startOnboarding(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        // 이미 온보딩 상태가 있는지 확인
        return onboardingRepository.findByUserId(userId)
                .orElseGet(() -> {
                    OnboardingStatus status = OnboardingStatus.builder()
                            .user(user)
                            .build();
                    log.info("Started onboarding for user {}", userId);
                    return onboardingRepository.save(status);
                });
    }
    
    /**
     * 기본 프로필 저장
     */
    @Transactional
    public OnboardingStatus saveProfile(Long userId, OnboardingProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        // User 엔티티 업데이트
        user.setName(dto.getName());
        user.setBirthDate(dto.getBirthDate());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setProfileImageUrl(dto.getProfileImageUrl());
        
        userRepository.save(user);
        
        // 온보딩 상태 업데이트
        OnboardingStatus status = getOrCreateStatus(userId);
        status.setProfileCompleted(true);
        status.checkAndUpdateCompletion();
        
        log.info("Saved profile for user {}", userId);
        return onboardingRepository.save(status);
    }
    
    /**
     * 건강 정보 저장
     */
    @Transactional
    public OnboardingStatus saveHealth(Long userId, OnboardingHealthDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        // UserDetail 생성 또는 조회
        UserDetail detail = userDetailRepository.findByUser_Id(userId)
                .orElse(UserDetail.builder().user(user).build());
        
        try {
            // JSON 변환하여 저장
            if (dto.getChronicDiseases() != null) {
                detail.setChronicDiseases(objectMapper.writeValueAsString(dto.getChronicDiseases()));
            }
            if (dto.getMedications() != null) {
                detail.setMedications(objectMapper.writeValueAsString(dto.getMedications()));
            }
            if (dto.getAllergies() != null) {
                detail.setAllergies(objectMapper.writeValueAsString(dto.getAllergies()));
            }
            
            detail.setCognitiveStatus(dto.getCognitiveStatus());
            detail.setMobilityStatus(dto.getMobilityStatus());
            detail.setHealthNotes(dto.getNotes());
            
            userDetailRepository.save(detail);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to convert health data to JSON", e);
            throw CustomException.internalServerError("건강 정보 저장 중 오류가 발생했습니다.");
        }
        
        // 온보딩 상태 업데이트
        OnboardingStatus status = getOrCreateStatus(userId);
        status.setHealthCompleted(true);
        status.checkAndUpdateCompletion();
        
        log.info("Saved health information for user {}", userId);
        return onboardingRepository.save(status);
    }
    
    /**
     * 보호자 정보 저장
     */
    @Transactional
    public OnboardingStatus saveGuardians(Long userId, OnboardingGuardiansDto dto) {
        User elderly = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        if (dto.getGuardians() != null && !dto.getGuardians().isEmpty()) {
            // 기존 보호자 정보 삭제 (업데이트 시)
            // guardianRepository.deleteByElderlyId(userId);
            
            for (OnboardingGuardiansDto.GuardianInfo guardianInfo : dto.getGuardians()) {

                
                log.info("Saved guardian info: {} for user {}", guardianInfo.getName(), userId);
            }
        }
        
        // 온보딩 상태 업데이트
        OnboardingStatus status = getOrCreateStatus(userId);
        status.setGuardiansCompleted(true);
        status.checkAndUpdateCompletion();
        
        log.info("Saved guardians information for user {}", userId);
        return onboardingRepository.save(status);
    }
    
    /**
     * 선호도 및 생활패턴 저장
     */
    @Transactional
    public OnboardingStatus savePreferences(Long userId, OnboardingPreferencesDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        // UserDetail 생성 또는 조회
        UserDetail detail = userDetailRepository.findByUser_Id(userId)
                .orElse(UserDetail.builder().user(user).build());
        
        try {
            // JSON 변환하여 저장
            if (dto.getInterests() != null) {
                detail.setInterests(objectMapper.writeValueAsString(dto.getInterests()));
            }
            if (dto.getFavoriteActivities() != null) {
                detail.setFavoriteActivities(objectMapper.writeValueAsString(dto.getFavoriteActivities()));
            }
            if (dto.getFavoriteFoods() != null) {
                detail.setFavoriteFoods(objectMapper.writeValueAsString(dto.getFavoriteFoods()));
            }
            if (dto.getMemorableMoments() != null) {
                detail.setMemorableMoments(objectMapper.writeValueAsString(dto.getMemorableMoments()));
            }
            
            // 생활 패턴
            detail.setWakeUpTime(dto.getWakeUpTime());
            detail.setBedTime(dto.getBedTime());
            detail.setBreakfastTime(dto.getBreakfastTime());
            detail.setLunchTime(dto.getLunchTime());
            detail.setDinnerTime(dto.getDinnerTime());
            
            detail.setCommunicationStyle(dto.getCommunicationStyle());
            detail.setReligion(dto.getReligion());
            
            userDetailRepository.save(detail);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to convert preferences data to JSON", e);
            throw CustomException.internalServerError("선호도 정보 저장 중 오류가 발생했습니다.");
        }
        
        // 온보딩 상태 업데이트
        OnboardingStatus status = getOrCreateStatus(userId);
        status.setPreferencesCompleted(true);
        status.checkAndUpdateCompletion();
        
        log.info("Saved preferences for user {}", userId);
        return onboardingRepository.save(status);
    }
    
    /**
     * 온보딩 상태 조회
     */
    public OnboardingStatus getOnboardingStatus(Long userId) {
        return onboardingRepository.findByUserId(userId)
                .orElseThrow(() -> CustomException.notFound("온보딩 정보를 찾을 수 없습니다."));
    }
    
    /**
     * 온보딩 완료 응답 생성
     */
    public OnboardingCompleteResponseDto getCompleteResponse(Long userId) {
        OnboardingStatus status = getOnboardingStatus(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        return OnboardingCompleteResponseDto.builder()
                .userId(userId)
                .completed(status.getIsCompleted())
                .progress(status.calculateProgress())
                .completedSteps(OnboardingCompleteResponseDto.OnboardingSteps.builder()
                        .profile(status.getProfileCompleted())
                        .health(status.getHealthCompleted())
                        .guardians(status.getGuardiansCompleted())
                        .preferences(status.getPreferencesCompleted())
                        .build())
                .completedAt(status.getCompletedAt())
                .welcomeMessage(status.getIsCompleted() ? 
                        user.getName() + "님, 마이루미에 오신 것을 환영합니다! 이제 AI 비서와 함께 하루를 시작해보세요." : 
                        "온보딩을 계속 진행해주세요.")
                .build();
    }
    
    /**
     * 온보딩 상태 가져오기 또는 생성
     */
    private OnboardingStatus getOrCreateStatus(Long userId) {
        return onboardingRepository.findByUserId(userId)
                .orElseGet(() -> startOnboarding(userId));
    }
}