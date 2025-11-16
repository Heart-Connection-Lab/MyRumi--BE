package com.myrumi.domain.user.dto;

import com.myrumi.domain.user.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 사용자 등록 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    
    @NotBlank(message = "사용자명은 필수입니다.")
    @Size(min = 4, max = 50, message = "사용자명은 4-50자 사이여야 합니다.")
    private String username;
    
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;
    
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
    private String name;
    
    private LocalDate birthDate;
    
    private User.Gender gender;
    
    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
             message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;
    
    @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
    private String address;
    
    @NotNull(message = "사용자 역할은 필수입니다.")
    private User.UserRole role;
}