package com.JH.JhOnlineJudge.domain.chat;

import com.JH.JhOnlineJudge.common.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final RedisHelper redisHelper;

    public List<ChatMessage> getRecentMessages(String roomId) {
        String key = "chat:" + roomId;
        List<ChatMessage> chatMessages = redisHelper.getListData("chat:" + roomId, ChatMessage.class);
        return chatMessages;
    }
}
