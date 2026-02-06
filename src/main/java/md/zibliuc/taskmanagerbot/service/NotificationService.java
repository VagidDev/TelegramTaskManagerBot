package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.context.UserState;
import md.zibliuc.taskmanagerbot.core.KeyboardService;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.database.entity.User;
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

    private final TelegramBot telegramBot;
    private final TaskService taskService;
    private final KeyboardService keyboardService;
    private final UserStateService userStateService;

    @Scheduled(fixedRate = 60_000)
    public void sendNotifications() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusSeconds(65); //for exclude loosing tasks between sending notifications
        LOGGER.debug("Start sending notifications for date from {} to {}", start, end);

        List<Task> tasks = taskService.getIncompletedTasksWithNotificationsForDateRange(start, end);
        LOGGER.info("Got {} task for send notifications", tasks.size());

        tasks.forEach(task ->
                EXECUTOR_SERVICE.submit(() -> {
                            try {
                                User user = task.getUser();

                                LOGGER.debug(
                                        "Trying to send notification to user {} for task with id {} in chat with id {}",
                                        user.getUsername(),
                                        task.getId(),
                                        user.getChatId()
                                );
                                telegramBot.execute(
                                        new SendMessage(
                                                user.getChatId().longValue(),
                                                "Время приступить к заданию:\n" + task.getName())
                                                .replyMarkup(keyboardService.replyForNotificationKeyboard(task.getId()))
                                );

                                userStateService.get(user.getChatId()).setState(UserState.WAITING_TASK_ACTION);
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
