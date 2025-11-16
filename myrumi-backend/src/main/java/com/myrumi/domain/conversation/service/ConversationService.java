package com.myrumi.domain.conversation.service;

import com.myrumi.common.exception.CustomException;
import com.myrumi.domain.conversation.entity.Conversation;
import com.myrumi.domain.conversation.entity.Message;
import com.myrumi.domain.conversation.repository.ConversationRepository;
import com.myrumi.domain.conversation.repository.MessageRepository;
import com.myrumi.domain.user.entity.User;
import com.myrumi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationService {
    
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    
    /**
     * 대화 생성
     */
    @Transactional
    public Conversation createConversation(Long userId, String title, Conversation.ConversationType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
        
        Conversation conversation = Conversation.builder()
                .user(user)
                .title(title)
                .type(type)
                .lastMessageAt(LocalDateTime.now())
                .build();
        
        return conversationRepository.save(conversation);
    }
    
    /**
     * 메시지 추가
     */
    @Transactional
    public Message addMessage(Long conversationId, Message.MessageRole role, String content, 
                              Message.MessageType messageType) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> CustomException.notFound("대화를 찾을 수 없습니다."));
        
        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .messageType(messageType)
                .build();
        
        conversation.addMessage(message);
        
        return messageRepository.save(message);
    }
    
    /**
     * 음성 메시지 추가
     */
    @Transactional
    public Message addVoiceMessage(Long conversationId, Message.MessageRole role, 
                                   String content, String audioUrl, Integer audioDuration) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> CustomException.notFound("대화를 찾을 수 없습니다."));
        
        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .messageType(Message.MessageType.VOICE)
                .audioUrl(audioUrl)
                .audioDuration(audioDuration)
                .build();
        
        conversation.addMessage(message);
        
        return messageRepository.save(message);
    }
    
    /**
     * 사용자의 대화 목록 조회
     */
    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findByUserIdOrderByLastMessageAtDesc(userId);
    }
    
    /**
     * 대화의 메시지 목록 조회 (페이징)
     */
    public Page<Message> getConversationMessages(Long conversationId, Pageable pageable) {
        return messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);
    }
    
    /**
     * 대화 조회
     */
    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> CustomException.notFound("대화를 찾을 수 없습니다."));
    }
    
    /**
     * 대화 삭제
     */
    @Transactional
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }
    
    /**
     * 메시지에 감정 분석 결과 업데이트
     */
    @Transactional
    public void updateMessageEmotion(Long messageId, String emotion, Double score) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> CustomException.notFound("메시지를 찾을 수 없습니다."));
        
        message.setDetectedEmotion(emotion);
        message.setEmotionScore(score);
    }
}