package main.java.com.myrumi.backend.dto;

import com.myrumi.backend.entity.Conversation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ConversationResponse {
    
    private Long id;
    private String userMessage;
    private String aiResponse;
    private String intent;
    private String emotion;
    private LocalDateTime createdAt;
    
    public static ConversationResponse from(Conversation conversation) {
        return ConversationResponse.builder()
                .id(conversation.getId())
                .userMessage(conversation.getUserMessage())
                .aiResponse(conversation.getAiResponse())
                .intent(conversation.getIntent())
                .emotion(conversation.getEmotion())
                .createdAt(conversation.getCreatedAt())
                .build();
    }
}