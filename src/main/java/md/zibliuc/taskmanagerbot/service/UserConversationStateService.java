package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserConversationStateService {
    private final Map<Long, ConversationContext> userConversationContext = new ConcurrentHashMap<>();

    public ConversationContext get(Long chatId) {
        return userConversationContext.computeIfAbsent(chatId, id -> {
            ConversationContext ctx = new ConversationContext();
            ctx.setChatId(id);
            return ctx;
        });
    }

    public void reset(Long chatId) {
        userConversationContext.remove(chatId);
    }

}
