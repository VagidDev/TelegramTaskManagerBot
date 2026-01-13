package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.database.entity.User;
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

    public User getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User getByChatId(Long chatId) {
        return repository.findByChatId(chatId).orElse(null);
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        for (User user :  repository.findAll()) {
            users.add(user);
        }

        return users;
    }

    public void save(User user) {
        repository.save(user);
    }
}
