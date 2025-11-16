package com.myrumi.domain.conversation.dto;

import com.myrumi.domain.conversation.entity.Conversation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationCreateDto {
    
    @NotNull(message = "사용자 ID는 필수입니다.")
    private Long userId;
    
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    
    @NotNull(message = "대화 유형은 필수입니다.")
    private Conversation.ConversationType type;
}