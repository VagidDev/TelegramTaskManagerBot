package md.zibliuc.taskmanagerbot.database.repository;

import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskCrudRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.deadline >= :start AND t.deadline <= :end AND t.isCompleted = :isCompleted")
    List<Task> findByDeadlineBetweenAndIsCompleted(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("isCompleted") boolean isCompleted
    );
}
