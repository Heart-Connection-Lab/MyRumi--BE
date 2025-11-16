package com.myrumi.domain.conversation.dto;

import com.myrumi.domain.conversation.entity.Conversation;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponseDto {
    
    private Long id;
    private Long userId;
    private String title;
    private Conversation.ConversationType type;
    private Integer messageCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastMessageAt;
    
    public static ConversationResponseDto from(Conversation conversation) {
        return ConversationResponseDto.builder()
                .id(conversation.getId())
                .userId(conversation.getUser().getId())
                .title(conversation.getTitle())
                .type(conversation.getType())
                .messageCount(conversation.getMessages().size())
                .createdAt(conversation.getCreatedAt())
                .lastMessageAt(conversation.getLastMessageAt())
                .build();
    }
}