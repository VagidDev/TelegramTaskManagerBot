package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.context.UserContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {
    private final Map<Long, UserContext> users = new ConcurrentHashMap<>();

    public UserContext get(Long chatId) {
        return users.computeIfAbsent(chatId, id -> new UserContext());
    }

    public void reset(Long chatId) {
        users.remove(chatId);
    }

}
