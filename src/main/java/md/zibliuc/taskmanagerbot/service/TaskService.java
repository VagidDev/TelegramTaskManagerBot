package md.zibliuc.taskmanagerbot.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.database.repository.TaskCrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskService {
    private static final Logger LOGGER = LogManager.getLogger(TaskService.class);
    private final TaskCrudRepository repository;
    private final UserService userService;

    public Task get(Long id) {
        if (id == null) {
            LOGGER.warn("Cannot find task with id `null`");
            return null;
        }
        return repository.findById(id).orElse(null);
    }

    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        Iterable<Task> taskIterable = repository.findAll();
        for (Task task : taskIterable) {
            tasks.add(task);
        }
        return tasks;
    }

    public List<Task> getIncompletedTasksWithNotificationsForDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDeadlineBetweenAndIsCompletedAndSendNotification(start, end, false, true);
    }

    public void save(Task task) {
        if (task == null) {
            LOGGER.warn("Cannot save `null` task!");
            return;
        }

        repository.save(task);
    }

    public void save(Long chatId, String title, LocalDateTime dateTime) {
        BotUser botUser = userService.getByChatId(chatId);

        if (botUser == null) {
            LOGGER.warn("No user for task with chatID `{}`", chatId);
            return;
        }

        Task task = new Task();

        task.setBotUser(botUser);
        task.setName(title);
        task.setDeadline(dateTime);

        save(task);
    }

    @Transactional
    public void completeTask(Long id) {
        Task task = get(id);
        if (task != null) {
            task.setCompleted(true);
        } else {
            LOGGER.warn("No task to complete with ID `{}`", id);
        }
    }

    @Transactional
    public void turnOffNotification(Task task) {
        task.setSendNotification(false);
    }

    @Transactional
    public void postponeTask(Long id, Long postponeOnMinutes) {
        if (id == null || postponeOnMinutes == null) {
            LOGGER.error(
                    "Cannot postpone task because of null argument. Task id -> {}, minutes for postponing -> {}",
                            id,
                            postponeOnMinutes
                    );
            return;
        }

        Task task = get(id);
        if (task == null) {
            LOGGER.error("Cannot find task with id {}", id);
            return;
        }

        LocalDateTime deadline = LocalDateTime.now().plusMinutes(postponeOnMinutes);
        task.setDeadline(deadline);
        task.setSendNotification(true);
    }

    @Deprecated
    public boolean update(Task task) {
        if (task == null) {
            LOGGER.warn("Cannot update `null` task!");
            return false;
        }

        if (task.getId() == null) {
            LOGGER.warn("Cannot update task with `null` id!");
            return false;
        }

        Task updatedTask = repository.save(task);
        return updatedTask.getId() != null;
    }

    @Transactional
    public void delete(Long id) {
        Task task = get(id);
        if (task != null)
            task.getBotUser()
                    .getTasks()
                    .removeIf(t -> Objects.equals(t.getId(), id));
    }
}
