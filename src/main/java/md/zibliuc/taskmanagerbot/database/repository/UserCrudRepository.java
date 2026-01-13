package md.zibliuc.taskmanagerbot.database.repository;

import md.zibliuc.taskmanagerbot.database.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
}
