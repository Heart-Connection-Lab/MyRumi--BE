package com.myrumi.domain.conversation.dto;

import com.myrumi.domain.conversation.entity.Message;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    
    private Long id;
    private Long conversationId;
    private Message.MessageRole role;
    private String content;
    private Message.MessageType messageType;
    private String audioUrl;
    private Integer audioDuration;
    private String detectedEmotion;
    private Double emotionScore;
    private LocalDateTime createdAt;
    
    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .role(message.getRole())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .audioUrl(message.getAudioUrl())
                .audioDuration(message.getAudioDuration())
                .detectedEmotion(message.getDetectedEmotion())
                .emotionScore(message.getEmotionScore())
                .createdAt(message.getCreatedAt())
                .build();
    }
}