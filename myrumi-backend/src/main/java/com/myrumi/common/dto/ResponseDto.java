package com.myrumi.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/* API 응답 형식 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
    
    /* 성공 여부 */
    private boolean success;
    
    /* 응답 메시지 */
    private String message;
    
    /* 응답 데이터 */
    private T data;
    
    /* 응답 시간 */
    private LocalDateTime timestamp;
    
    /*  성공 응답 (데이터 포함) */
    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.<T>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /*성공 응답 (데이터 + 커스텀 메시지) */
    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /* 에러 응답 */
    public static <T> ResponseDto<T> error(String message) {
        return ResponseDto.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /*에러 응답 (데이터 포함)*/
    public static <T> ResponseDto<T> error(String message, T data) {
        return ResponseDto.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}