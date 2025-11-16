package com.myrumi.domain.onboarding.dto;

import com.myrumi.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

/**
 * 온보딩 - 기본 프로필 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnboardingProfileDto {
    
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
    private String name;
    
    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;
    
    @NotNull(message = "성별은 필수입니다.")
    private User.Gender gender;
    
    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
             message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;
    
    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
    private String address;
    
    /**
     * 프로필 사진 URL (선택)
     */
    private String profileImageUrl;
}