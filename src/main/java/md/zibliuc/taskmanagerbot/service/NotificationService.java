package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(8);

    private final TelegramBot telegramBot;
    private final TaskService taskService;

    public NotificationService(TelegramBot telegramBot, TaskService taskService) {
        this.telegramBot = telegramBot;
        this.taskService = taskService;
    }

    @Scheduled(fixedRate = 60_000)
    public void sendNotifications() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);
        LOGGER.debug("Start sending notifications for date from {} to {}", start, end);

        List<Task> tasks = taskService.getIncompletedTasksWithNotificationsForDateRange(start, end);
        LOGGER.info("Got {} task for send notifications", tasks.size());

        tasks.forEach(task ->
                EXECUTOR_SERVICE.submit(() -> {
                            try {
                                LOGGER.debug(
                                        "Trying to send notification to user {} for task with id {} in chat with id {}",
                                        task.getUser().getUsername(),
                                        task.getId(),
                                        task.getUser().getChatId()
                                );
                                telegramBot.execute(
                                        new SendMessage(
                                                task.getUser().getChatId().longValue(),
                                                "Время приступить к заданию:\n" + task.getName()));

                                taskService.turnOffNotification(task);
                            } catch (Exception e) {
                                LOGGER.error(
                                        "Error occurred while trying to send notification of task {} to user {} with chat id {}. Error -> {}",
                                        task.getId(),
                                        task.getUser().getId(),
                                        task.getUser().getChatId(),
                                        e.getLocalizedMessage(),
                                        e
                                );
                            }
                        }
                ));
    }
}
