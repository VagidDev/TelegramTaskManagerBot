package md.zibliuc.taskmanagerbot.database.repository;

import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<BotUser, Long> {
    Optional<BotUser> findByChatId(Long chatId);
}
