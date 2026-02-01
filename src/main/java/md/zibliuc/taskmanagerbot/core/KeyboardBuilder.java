package md.zibliuc.taskmanagerbot.core;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import md.zibliuc.taskmanagerbot.database.entity.Task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KeyboardBuilder {
    public static InlineKeyboardMarkup dateKeyboard() {
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

    public static InlineKeyboardMarkup crudKeyboard(Long taskId) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();

        kb.addRow(new InlineKeyboardButton("Выполнить")
                .callbackData("COMPLETE:" + taskId));
        kb.addRow(new InlineKeyboardButton("Удалить")
                .callbackData("DELETE:" + taskId));
        kb.addRow(new InlineKeyboardButton("Изменить")
                .callbackData("EDIT:" + taskId));
        kb.addRow(new InlineKeyboardButton("Отмена")
                .callbackData("CANCEL:" + taskId));

        return kb;
    }

    public static ReplyKeyboardMarkup menuKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton("Создать задачу"),
                new KeyboardButton("Мои задачи"),
                new KeyboardButton("Невыполненные задачи"),
                new KeyboardButton("Помощь")
        )
                .resizeKeyboard(true)
                .selective(true);
    }

    public static InlineKeyboardMarkup taskKeyboard(List<Task> tasks) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        tasks.forEach(task -> keyboard.addRow(
                        new InlineKeyboardButton(task.getName())
                                .callbackData("TASK:" + task.getId().toString())
                )
        );
        return keyboard;
    }
}
