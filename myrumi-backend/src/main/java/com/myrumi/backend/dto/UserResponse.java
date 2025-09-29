package main.java.com.myrumi.backend.dto;

import com.myrumi.backend.entity.User;
import com.myrumi.backend.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    
    private Long id;
    private String username;
    private String name;
    private String address;
    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;
    
    // Entity를 DTO로 변환
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}