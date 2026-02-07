package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {
    private final Map<Long, ConversationContext> users = new ConcurrentHashMap<>();

    public ConversationContext get(Long chatId) {
        return users.computeIfAbsent(chatId, id -> new ConversationContext());
    }

    public void reset(Long chatId) {
        users.remove(chatId);
    }

}
