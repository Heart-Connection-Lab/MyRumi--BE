package main.java.com.myrumi.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ConversationRequest {
    
    private Long userId;
    private String userMessage;
    
}