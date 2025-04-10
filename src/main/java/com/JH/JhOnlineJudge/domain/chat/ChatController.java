package com.JH.JhOnlineJudge.domain.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatPublisher redisPublisher;
    private final ChatService chatService;
    @MessageMapping("/chat.send")
    public void send(ChatMessage message) {
        redisPublisher.publish(message);
    }

    @GetMapping("/chat/history")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@RequestParam String roomId) {
        return ResponseEntity.ok(chatService.getRecentMessages(roomId));
    }

}
