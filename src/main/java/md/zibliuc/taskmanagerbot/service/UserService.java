package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.database.repository.UserCrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserCrudRepository repository;

    public UserService(UserCrudRepository repository) {
        this.repository = repository;
    }

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

    public void save(BotUser botUser) {
        repository.save(botUser);
    }
}
