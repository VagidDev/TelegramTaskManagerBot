package md.zibliuc.taskmanagerbot.service;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.database.entity.User;
import md.zibliuc.taskmanagerbot.database.repository.TaskCrudRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Task> getCurrentTasksForNotification() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);

        return repository.findByDeadlineBetweenAndIsCompleted(start, end, false).forEach(task -> System.out.println(task.getName()));
    }

    public void save(Task task) {
        if (task == null) {
            LOGGER.warn("Cannot save `null` task!");
            return;
        }

        repository.save(task);
    }

    public void save(Long chatId, String title, LocalDateTime dateTime) {
        User user = userService.getByChatId(chatId);

        if (user == null) {
            LOGGER.warn("No user for task with chatID `{}`", chatId);
            return;
        }

        Task task = new Task();

        task.setUser(user);
        task.setName(title);
        task.setDeadline(dateTime);

        save(task);
    }

    public void completeTask(Long id) {
        Task task = get(id);
        if (task != null) {
            task.setCompleted(true);
            save(task);
        } else {
            LOGGER.warn("No task to complete with ID `{}`", id);
        }
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

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
