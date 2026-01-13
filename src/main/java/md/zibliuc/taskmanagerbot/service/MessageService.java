package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.context.TaskAction;
import md.zibliuc.taskmanagerbot.context.UserContext;
import md.zibliuc.taskmanagerbot.context.UserState;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final TelegramBot telegramBot;
    private final UserStateService userStateService;
    private final TaskService taskService;
    private final UserService userService;

    public void processTextMessage(Update update) {
        Long chatId = update.message().chat().id();
        String text = update.message().text();
        UserContext context = userStateService.get(chatId);
        if (context.getState() == UserState.IDLE) {
            proceedSimpleCommand(update, context, text);
        } else {
            proceedBuildTaskCommand(update, chatId, text, context);
        }
    }

    // TODO
    public void proceedCallbackQuery(Update update) {
        Long chatId = update.callbackQuery().maybeInaccessibleMessage().chat().id();
        Integer messageId = update.callbackQuery().maybeInaccessibleMessage().messageId();
        String text = update.callbackQuery().data();
        UserContext context = userStateService.get(chatId);

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

    public void proceedSimpleCommand(Update update, UserContext context, String command) {
        Long chatId = update.message().chat().id();
        String response = switch (command) {
            case "/start" ->  {
                User user = update.message().from();
                createUser(user, chatId);
                yield "Привет! Я Task Manager бот";
            }
            case "/create" -> {
                context.setState(UserState.WAITING_TITLE);
                yield "Введите свое задание";
            }
            case "/show" -> {
                context.setState(UserState.WAITING_TASK);
                md.zibliuc.taskmanagerbot.database.entity.User user = userService.getByChatId(chatId);
                if (user != null) {
                    showTaskList(chatId, user.getTasks());
                }
                yield "";
            }
            case "/help" -> "Доступные команды: /start, /help";
            default -> "Ты написал: " + command;
        };
        if (!response.isEmpty())
            telegramBot.execute(new SendMessage(chatId.longValue(), response));
    }

    public void proceedBuildTaskCommand(Update update, Long chatId, String text, UserContext context) {
        switch (context.getState()) {
            case WAITING_TITLE -> {
                context.setTitle(text);
                context.setState(UserState.WAITING_DATE);

                telegramBot.execute(new SendMessage(chatId.longValue(),
//                        "Выберите дату, либо введите в ручную в формате (dd MM)")
                          "Выберите дату")
                        .replyMarkup(dateKeyboard())
                );
            }
            case WAITING_DATE -> {
                if (text.startsWith("DATE:")) {
                    LocalDate date = text.equals("DATE:TODAY")
                            ? LocalDate.now()
                            : LocalDate.parse(text.replace("DATE:", ""));

                    context.setDate(date);
                    context.setState(UserState.WAITING_TIME);
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
                                + "Время: " + deadline
                ));

                userStateService.reset(chatId);
            }
        }
    }
    //TODO: add implementation for edit and delete action
    public void proceedTaskAction(Long chatId, Integer messageId, String callBackData, UserContext context) {
        if(callBackData.startsWith("CANCEL")) {
            userStateService.reset(chatId);
            return;
        }

        String[] data = callBackData.split(":");
        if (data.length != 2) {
            return;
        }

        TaskAction action = TaskAction.valueOf(data[0]);
        switch (action) {
            case COMPLETE -> {
                Long id = Long.valueOf(data[1]);
                taskService.completeTask(id);
            }
            case EDIT -> {

            }
        }
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

    public void showTaskList(Long chatId, List<Task> tasks) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        tasks.forEach(task -> keyboard.addRow(
                    new InlineKeyboardButton(task.getName())
                            .callbackData("TASK:" + task.getId().toString())
                )
        );

        telegramBot.execute(
                new SendMessage(chatId.longValue(), "Что будем делать?")
                        .replyMarkup(keyboard)
        );

    }

    public void proceedTask(Long chatId, Integer messageId, String callBackData, UserContext context) {
        if (!callBackData.startsWith("TASK:")) {
            return;
        }

        Long taskId = Long.valueOf(callBackData.replace("TASK:", ""));
        Task currentTask = taskService.get(taskId);

        if (currentTask != null) {
            telegramBot.execute(
                    new EditMessageText(chatId, messageId, "Задание: " + currentTask.getName())
                            .replyMarkup(crudKeyboard(chatId))
            );
            context.setState(UserState.WAITING_TASK_ACTION);
        }
    }

    public InlineKeyboardMarkup crudKeyboard(Long taskId) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();

        kb.addRow(new InlineKeyboardButton("Выполнить")
                        .callbackData("COMPLETE:" + taskId));
        kb.addRow(new InlineKeyboardButton("Удалить")
                .callbackData("DELETE:" + taskId));
        kb.addRow(new InlineKeyboardButton("Изменить")
                .callbackData("EDIT:" + taskId));
        kb.addRow(new InlineKeyboardButton("Отмена")
                .callbackData("CANCEL:"));

        return kb;
    }

    public InlineKeyboardMarkup dateKeyboard() {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();

        kb.addRow(
                new InlineKeyboardButton("Сегодня")
                        .callbackData("DATE:TODAY")
        );

        LocalDate today = LocalDate.now();
        for (int i = 1; i <= 5; i++) {
            LocalDate d = today.plusDays(i);
            kb.addRow(
                    new InlineKeyboardButton(d.format(DateTimeFormatter.ofPattern("dd MMM")))
                            .callbackData("DATE:" + d)
            );
        }

        kb.addRow(
                new InlineKeyboardButton("Отмена")
                        .callbackData("CANCEL")
        );

        return kb;
    }
}
