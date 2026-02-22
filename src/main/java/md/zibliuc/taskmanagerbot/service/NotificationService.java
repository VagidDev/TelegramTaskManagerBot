package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.database.entity.BotUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private final TelegramBot telegramBot;
    private final TaskService taskService;
    private final KeyboardService keyboardService;

    //@Scheduled(fixedRate = 60_000)
    public void sendNotifications() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusSeconds(65); //for exclude loosing tasks between sending notifications
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
                                telegramBot.execute(
                                        new SendMessage(
                                                botUser.getChatId().longValue(),
                                                "Время приступить к заданию:\n" + task.getName())
                                                .replyMarkup(keyboardService.replyForNotificationKeyboard(task.getId()))
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
