package md.zibliuc.taskmanagerbot.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.database.repository.UserCrudRepository;
import md.zibliuc.taskmanagerbot.dto.TelegramUserData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final UserCrudRepository repository;

    public BotUser getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public BotUser getByChatId(Long chatId) {
        return repository.findByChatId(chatId).orElse(null);
    }

    public List<BotUser> getAll() {
        List<BotUser> botUsers = new ArrayList<>();

        for (BotUser botUser :  repository.findAll()) {
            botUsers.add(botUser);
        }

        return botUsers;
    }

    public void ensureUserExists(TelegramUserData userData) {
        if (userData == null) return;

        BotUser user = getByChatId(userData.chatId());
        if (user == null) {
            LOGGER.info(
                    "User for chat id {} does not exists. Creating user with username {}",
                    userData.chatId(),
                    userData.username()
            );
            user = new BotUser(
                    null,
                    userData.chatId(),
                    userData.firstName(),
                    userData.lastName(),
                    userData.username(),
                    null
            );

            save(user);
        } else {
            LOGGER.info("User for chat id {} already exists", userData.chatId());
        }
    }

    public void save(BotUser botUser) {
        repository.save(botUser);
    }
}
