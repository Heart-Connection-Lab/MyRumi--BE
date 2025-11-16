package com.myrumi.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 사용자 정보 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    
    @Size(max = 50, message = "이름은 50자를 초과할 수 없습니다.")
    private String name;
    
    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
             message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;
    
    @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
    private String address;
    
    private String profileImageUrl;
}