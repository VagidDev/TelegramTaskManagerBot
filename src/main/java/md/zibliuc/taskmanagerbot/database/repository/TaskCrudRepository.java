package md.zibliuc.taskmanagerbot.database.repository;

import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCrudRepository extends CrudRepository<Task, Long> {

}
