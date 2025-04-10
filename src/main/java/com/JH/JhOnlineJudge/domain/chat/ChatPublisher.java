    package com.JH.JhOnlineJudge.domain.chat;

    import com.JH.JhOnlineJudge.common.utils.RedisHelper;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.redis.listener.ChannelTopic;
    import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class ChatPublisher {
        private final RedisHelper redisHelper;
        private final ChannelTopic topic;


        public void publish(ChatMessage message) {
            redisHelper.saveDataToList("chat:" + message.getRoomId(), message, 49);
            redisHelper.publishData(topic.getTopic(), message);
        }

    }
