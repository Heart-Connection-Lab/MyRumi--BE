package com.myrumi.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


/* 커스텀 예외 클래스 */
@Getter
public class CustomException extends RuntimeException {
    
    /* HTTP 상태 코드 */
    private final HttpStatus status;
    
    /* 에러 코드  */
    private final String errorCode;
    
    /* 기본 생성자 */
    public CustomException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = null;
    }
    
    /* HTTP 상태 코드를 지정하는 생성자 */
    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = null;
    }
    
    /*HTTP 상태 코드와 에러 코드를 지정하는 생성자*/
    public CustomException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    
    /*예외 생성 메서드들*/
    
    public static CustomException notFound(String message) {
        return new CustomException(message, HttpStatus.NOT_FOUND);
    }
    
    public static CustomException badRequest(String message) {
        return new CustomException(message, HttpStatus.BAD_REQUEST);
    }
    
    public static CustomException unauthorized(String message) {
        return new CustomException(message, HttpStatus.UNAUTHORIZED);
    }
    
    public static CustomException forbidden(String message) {
        return new CustomException(message, HttpStatus.FORBIDDEN);
    }
    
    public static CustomException conflict(String message) {
        return new CustomException(message, HttpStatus.CONFLICT);
    }
    
    public static CustomException internalServerError(String message) {
        return new CustomException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}