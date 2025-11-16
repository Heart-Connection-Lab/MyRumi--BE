package com.myrumi.domain.user.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.user.dto.UserRegisterDto;
import com.myrumi.domain.user.dto.UserResponseDto;
import com.myrumi.domain.user.dto.UserUpdateDto;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<ResponseDto<UserResponseDto>> register(
            @Valid @RequestBody UserRegisterDto dto) {
        
        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .name(dto.getName())
                .birthDate(dto.getBirthDate())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(dto.getRole())
                .build();
        
        User createdUser = userService.createUser(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(UserResponseDto.from(createdUser)));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 사용자 정보를 조회합니다.")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(ResponseDto.success(UserResponseDto.from(user)));
    }
    
    @GetMapping("/elderly")
    @Operation(summary = "노인 사용자 목록", description = "모든 노인 사용자 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getElderlyUsers() {
        List<UserResponseDto> users = userService.findAllElderlyUsers().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(users));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto) {
        
        User updateData = User.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
        
        User updatedUser = userService.updateUser(id, updateData);
        
        return ResponseEntity.ok(ResponseDto.success(UserResponseDto.from(updatedUser)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "사용자를 비활성화합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ResponseDto.success(null, "사용자가 비활성화되었습니다."));
    }
}