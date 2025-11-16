package com.myrumi.domain.conversation.dto;

import com.myrumi.domain.conversation.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCreateDto {
    
    @NotNull(message = "메시지 역할은 필수입니다.")
    private Message.MessageRole role;
    
    @NotBlank(message = "메시지 내용은 필수입니다.")
    private String content;
    
    @NotNull(message = "메시지 유형은 필수입니다.")
    private Message.MessageType messageType;
    
    // 음성 메시지용
    private String audioUrl;
    private Integer audioDuration;
}