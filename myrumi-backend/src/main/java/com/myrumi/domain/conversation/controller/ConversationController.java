package com.myrumi.domain.conversation.controller;

import com.myrumi.common.dto.ResponseDto;
import com.myrumi.domain.conversation.dto.*;
import com.myrumi.domain.conversation.entity.Conversation;
import com.myrumi.domain.conversation.entity.Message;
import com.myrumi.domain.conversation.service.ConversationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@Tag(name = "Conversation", description = "대화 관리 API")
public class ConversationController {
    
    private final ConversationService conversationService;
    
    @PostMapping
    @Operation(summary = "대화 시작", description = "새로운 대화를 시작합니다.")
    public ResponseEntity<ResponseDto<ConversationResponseDto>> createConversation(
            @Valid @RequestBody ConversationCreateDto dto) {
        
        Conversation conversation = conversationService.createConversation(
                dto.getUserId(),
                dto.getTitle(),
                dto.getType()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(ConversationResponseDto.from(conversation)));
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 대화 목록", description = "사용자의 모든 대화 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<List<ConversationResponseDto>>> getUserConversations(
            @PathVariable Long userId) {
        
        List<ConversationResponseDto> conversations = conversationService
                .getUserConversations(userId)
                .stream()
                .map(ConversationResponseDto::from)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(ResponseDto.success(conversations));
    }
    
    @GetMapping("/{conversationId}")
    @Operation(summary = "대화 조회", description = "특정 대화를 조회합니다.")
    public ResponseEntity<ResponseDto<ConversationResponseDto>> getConversation(
            @PathVariable Long conversationId) {
        
        Conversation conversation = conversationService.getConversation(conversationId);
        
        return ResponseEntity.ok(ResponseDto.success(ConversationResponseDto.from(conversation)));
    }
    
    @PostMapping("/{conversationId}/messages")
    @Operation(summary = "메시지 전송", description = "대화에 새 메시지를 추가합니다.")
    public ResponseEntity<ResponseDto<MessageResponseDto>> sendMessage(
            @PathVariable Long conversationId,
            @Valid @RequestBody MessageCreateDto dto) {
        
        Message message;
        
        if (dto.getMessageType() == Message.MessageType.VOICE) {
            message = conversationService.addVoiceMessage(
                    conversationId,
                    dto.getRole(),
                    dto.getContent(),
                    dto.getAudioUrl(),
                    dto.getAudioDuration()
            );
        } else {
            message = conversationService.addMessage(
                    conversationId,
                    dto.getRole(),
                    dto.getContent(),
                    dto.getMessageType()
            );
        }
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(MessageResponseDto.from(message)));
    }
    
    @GetMapping("/{conversationId}/messages")
    @Operation(summary = "메시지 목록 조회", description = "대화의 메시지 목록을 조회합니다.")
    public ResponseEntity<ResponseDto<Page<MessageResponseDto>>> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<MessageResponseDto> messages = conversationService
                .getConversationMessages(conversationId, PageRequest.of(page, size))
                .map(MessageResponseDto::from);
        
        return ResponseEntity.ok(ResponseDto.success(messages));
    }
    
    @DeleteMapping("/{conversationId}")
    @Operation(summary = "대화 삭제", description = "대화를 삭제합니다.")
    public ResponseEntity<ResponseDto<Void>> deleteConversation(
            @PathVariable Long conversationId) {
        
        conversationService.deleteConversation(conversationId);
        
        return ResponseEntity.ok(ResponseDto.success(null, "대화가 삭제되었습니다."));
    }
}