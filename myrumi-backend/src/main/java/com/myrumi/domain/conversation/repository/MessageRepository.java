package com.myrumi.domain.conversation.repository;

import com.myrumi.domain.conversation.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * 대화 ID로 메시지 목록 조회 (최근 순, 페이징)
     */
    Page<Message> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);
    
    /**
     * 대화 ID와 기간으로 메시지 조회
     */
    List<Message> findByConversationIdAndCreatedAtBetween(
            Long conversationId, 
            LocalDateTime start, 
            LocalDateTime end
    );
}