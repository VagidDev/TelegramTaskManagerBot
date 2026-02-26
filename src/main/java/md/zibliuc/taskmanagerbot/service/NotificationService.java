package md.zibliuc.taskmanagerbot.service;

import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.bot.TelegramSender;
import md.zibliuc.taskmanagerbot.config.NotificationResponseConfig;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import md.zibliuc.taskmanagerbot.dto.OutgoingMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(8);

    private final NotificationResponseConfig notificationResponseConfig;
    private final KeyboardService keyboardService;
    private final TelegramSender telegramSender;
    private final TaskService taskService;

    @Scheduled(fixedRate = 60_000)
    public void sendNotifications() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusSeconds(60);
        LOGGER.debug("Start sending notifications for date from {} to {}", start, end);

        List<Task> tasks = taskService.getIncompletedTasksWithNotificationsForDateRange(start, end);
        LOGGER.info("Got {} task for send notifications", tasks.size());

        tasks.forEach(task ->
                EXECUTOR_SERVICE.submit(() -> {
                            try {
                                BotUser botUser = task.getBotUser();

                                LOGGER.debug(
                                        "Trying to send notification to user {} for task with id {} in chat with id {}",
                                        botUser.getUsername(),
                                        task.getId(),
                                        botUser.getChatId()
                                );

                                String response = notificationResponseConfig
                                        .getSendTaskNotification()
                                        .formatted(task.getName());

                                telegramSender.send(
                                        OutgoingMessage.send(
                                                botUser.getChatId(),
                                                response
                                        ).keyboard(keyboardService.replyForNotificationKeyboard(task.getId()))
                                );

                                taskService.turnOffNotification(task);
                            } catch (Exception e) {
                                LOGGER.error(
                                        "Error occurred while trying to send notification of task {} to user {} with chat id {}. Error -> {}",
                                        task.getId(),
                                        task.getBotUser().getId(),
                                        task.getBotUser().getChatId(),
                                        e.getLocalizedMessage(),
                                        e
                                );
                            }
                        }
                ));
    }
}
