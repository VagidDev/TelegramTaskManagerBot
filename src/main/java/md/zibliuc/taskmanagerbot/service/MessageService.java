package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.context.TaskAction;
import md.zibliuc.taskmanagerbot.conversation.ConversationContext;
import md.zibliuc.taskmanagerbot.conversation.ConversationState;
import md.zibliuc.taskmanagerbot.keyboard.KeyboardService;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class MessageService {
    private static final Logger LOGGER = LogManager.getLogger(MessageService.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm");

    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final TaskService taskService;
    private final UserService userService;
    private final KeyboardService keyboardService;

    public void processTextMessage(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        ConversationContext context = userStateService.get(chatId);
        if (EnumSet.of(ConversationState.WAITING_TITLE, ConversationState.WAITING_DATE, ConversationState.WAITING_TIME).contains(context.getState())) {
            proceedBuildTaskCommand(update, chatId, text, context);
        } else {
            proceedSimpleCommand(update, context, text);
        }
    }

    // TODO
    public void proceedCallbackQuery(Update update) {
        Long chatId = update.callbackQuery().maybeInaccessibleMessage().chat().id();
        Integer messageId = update.callbackQuery().maybeInaccessibleMessage().messageId();
        String text = update.callbackQuery().data();
        ConversationContext context = userStateService.get(chatId);

        switch (context.getState()) {
            case WAITING_DATE ->
                proceedBuildTaskCommand(update, chatId, text, context);
            case WAITING_TASK ->
                proceedTask(chatId, messageId, text, context);
            case WAITING_TASK_ACTION ->
                proceedTaskAction(chatId, messageId, text, context);

        }

        telegramBot.execute(new AnswerCallbackQuery(update.callbackQuery().id()));
    }

    public void proceedSimpleCommand(Update update, ConversationContext context, String command) {
        Long chatId = update.message().chat().id();
        switch (command) {
            case "/start" -> {
                User user = update.message().from();
                createUser(user, chatId);
                telegramBot.execute(new SendMessage(
                        chatId.longValue(),
                        "Привет! Я Task Manager бот"
                ).replyMarkup(keyboardService.menuKeyboard()));
            }
            case "/create", "Создать задачу" -> {
                context.setState(ConversationState.WAITING_TITLE);

                telegramBot.execute(
                        new SendMessage(
                                chatId.longValue(),
                                "Введите свое задание"
                                ).replyMarkup(new ReplyKeyboardRemove())
                );
            }
            case "/show", "Мои задачи"-> {
                context.setState(ConversationState.WAITING_TASK);
                md.zibliuc.taskmanagerbot.database.entity.User user = userService.getByChatId(chatId);
                if (user != null) {
                    telegramBot.execute(
                            new SendMessage(
                                    chatId.longValue(),
                                    "Что будем делать?"
                            ).replyMarkup(keyboardService.taskKeyboard(user.getTasks()))
                    );
                }
            }
            case "/uncompleted", "Невыполненные задачи" -> {
                context.setState(ConversationState.WAITING_TASK);
                md.zibliuc.taskmanagerbot.database.entity.User user = userService.getByChatId(chatId);
                if (user != null) {
                    telegramBot.execute(
                            new SendMessage(
                                    chatId.longValue(),
                                    "Что будем делать?"
                            ).replyMarkup(keyboardService.taskKeyboard(user.getUncompletedTask()))
                    );
                }
            }
            case "/help", "Помощь" -> telegramBot.execute(
                    new SendMessage(
                            chatId.longValue(),
                            "Доступные команды: /start, /help"
                    )
            );
            default -> telegramBot.execute(
                    new SendMessage(
                            chatId.longValue(),
                            "Ты написал: " + command
                    )
            );
        }

    }

    public void proceedBuildTaskCommand(Update update, Long chatId, String text, ConversationContext context) {
        switch (context.getState()) {
            case WAITING_TITLE -> {
                context.setTitle(text);
                context.setState(ConversationState.WAITING_DATE);

                telegramBot.execute(new SendMessage(chatId.longValue(),
                          "Выберите дату")
                        .replyMarkup(keyboardService.dateKeyboard())
                );
            }
            case WAITING_DATE -> {
                if (text.startsWith("DATE:")) {
                    LocalDate date = text.equals("DATE:TODAY")
                            ? LocalDate.now()
                            : LocalDate.parse(text.replace("DATE:", ""));

                    context.setDate(date);
                    context.setState(ConversationState.WAITING_TIME);
                    // TODO: replace editing message, or improve it
                    telegramBot.execute(new EditMessageText(
                            chatId,
                            update.callbackQuery().maybeInaccessibleMessage().messageId(),
                            "Введите время (HH:mm)"
                    ));
                }
            }
            case WAITING_TIME -> {
                LocalTime time;

                try {
                    time = LocalTime.parse(text);
                } catch (Exception e) {
                    telegramBot.execute(new SendMessage(
                            chatId.longValue(),
                            "Неверный формат. Пример: 09:30"
                    ));
                    return;
                }

                LocalDateTime deadline = LocalDateTime.of(context.getDate(), time);

                taskService.save(
                        chatId,
                        context.getTitle(),
                        deadline
                );

                telegramBot.execute(new SendMessage(
                        chatId.longValue(),
                        "Задача создана:\n"
                                + context.getTitle() + "\n"
                                + "Время: " + DATE_TIME_FORMATTER.format(deadline)
                ).replyMarkup(keyboardService.menuKeyboard()));

                userStateService.reset(chatId);
            }
            default -> userStateService.reset(chatId);
        }
    }
    //TODO: add implementation for edit
    public void proceedTaskAction(Long chatId, Integer messageId, String callBackData, ConversationContext context) {
        String[] data = callBackData.split(":");
        if (data.length != 2) {
            LOGGER.error("Cannot parse callback data for task action. Data: {}", callBackData);
            return;
        }

        TaskAction action = TaskAction.valueOf(data[0]);
        Long id = Long.valueOf(data[1]);
        switch (action) {
            case COMPLETE -> {
                taskService.completeTask(id);
                userStateService.reset(chatId);
            }
            case EDIT -> {
                //TODO: Implement it
            }
            case DELETE -> {
                taskService.delete(id);
                userStateService.reset(chatId);
            }
            case POSTPONE -> {
                taskService.postponeTask(id, 5L);
                userStateService.reset(chatId);
            }
            case CANCEL -> userStateService.reset(chatId);
        }

        telegramBot.execute(
                new DeleteMessage(chatId, messageId)
        );
    }

    public void createUser(User user, Long chatId) {
        md.zibliuc.taskmanagerbot.database.entity.User localUser = userService.getByChatId(chatId);

        if (localUser == null) {
            localUser =
                    new md.zibliuc.taskmanagerbot.database.entity.User(
                            null, chatId, user.firstName(), user.lastName(), user.username(), new ArrayList<>()
                    );
        }

        userService.save(localUser);
    }

    public void proceedTask(Long chatId, Integer messageId, String callBackData, ConversationContext context) {
        if (!callBackData.startsWith("TASK:")) {
            return;
        }

        Long taskId = Long.valueOf(callBackData.replace("TASK:", ""));
        Task currentTask = taskService.get(taskId);

        if (currentTask != null) {
            String answer = "Задание: " + currentTask.getName()
                    + "\nЗапланирован на: " + DATE_TIME_FORMATTER.format(currentTask.getDeadline());
            telegramBot.execute(
                    new EditMessageText(chatId, messageId, answer)
                            .replyMarkup(keyboardService.crudKeyboard(taskId))
            );
            context.setState(ConversationState.WAITING_TASK_ACTION);
        }
    }

}
