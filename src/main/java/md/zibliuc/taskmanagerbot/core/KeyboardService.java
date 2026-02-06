package md.zibliuc.taskmanagerbot.core;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.config.CrudMenuConfig;
import md.zibliuc.taskmanagerbot.config.MainMenuConfig;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {
    private static final Logger LOGGER = LogManager.getLogger(KeyboardService.class);

    private final MainMenuConfig mainMenuConfig;
    private final CrudMenuConfig crudMenuConfig;

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

    public InlineKeyboardMarkup crudKeyboard(Long taskId) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();
        LOGGER.debug(
                "Trying to create CRUD menu with values for complete -> {}, delete -> {}, edit -> {}, cancel -> {}",
                crudMenuConfig.getComplete(),
                crudMenuConfig.getDelete(),
                crudMenuConfig.getEdit(),
                crudMenuConfig.getCancel()
        );
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getComplete())
                .callbackData("COMPLETE:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getDelete())
                .callbackData("DELETE:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getEdit())
                .callbackData("EDIT:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getCancel())
                .callbackData("CANCEL:" + taskId));

        return kb;
    }

    public ReplyKeyboardMarkup menuKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getCreate())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getUncompleted())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getAll())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getHelp())}
        )
                .resizeKeyboard(true)
                .selective(true);
    }

    public InlineKeyboardMarkup taskKeyboard(List<Task> tasks) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        tasks.forEach(task -> keyboard.addRow(
                        new InlineKeyboardButton(task.getName())
                                .callbackData("TASK:" + task.getId().toString())
                )
        );
        return keyboard;
    }

    public InlineKeyboardMarkup replyForNotificationKeyboard(Long taskId) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();

        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getComplete())
                .callbackData("COMPLETE:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getPostpone())
                .callbackData("POSTPONE:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getCancel())
                .callbackData("CANCEL:" + taskId));

        return kb;
    }
}
