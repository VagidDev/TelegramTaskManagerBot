package md.zibliuc.taskmanagerbot.service;

import com.pengrad.telegrambot.model.request.*;
import lombok.RequiredArgsConstructor;
import md.zibliuc.taskmanagerbot.callback.CallbackType;
import md.zibliuc.taskmanagerbot.config.CrudMenuConfig;
import md.zibliuc.taskmanagerbot.config.DateMenuConfig;
import md.zibliuc.taskmanagerbot.config.MainMenuConfig;
import md.zibliuc.taskmanagerbot.database.entity.Task;
import md.zibliuc.taskmanagerbot.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyboardService {
    private static final Logger LOGGER = LogManager.getLogger(KeyboardService.class);

    private final MainMenuConfig mainMenuConfig;
    private final CrudMenuConfig crudMenuConfig;
    private final DateMenuConfig dateMenuConfig;
    private final DateTimeUtil dateTimeUtil;

    public InlineKeyboardMarkup dateKeyboard(int dateCount) {
        InlineKeyboardMarkup kb = new InlineKeyboardMarkup();
        LocalDate today = LocalDate.now();

        return createDateRangeKeyboard(kb, today, today.plusDays(dateCount));
    }

    public InlineKeyboardMarkup dateRangeKeyboard(LocalDate date, int countOfDays) {
        if (countOfDays > 0) {
            return createDateRangeKeyboard(new InlineKeyboardMarkup(), date, date.plusDays(countOfDays));
        } else {
            return createDateRangeKeyboard(new InlineKeyboardMarkup(), date.minusDays(countOfDays * -1), date);
        }
    }

    private InlineKeyboardMarkup createDateRangeKeyboard(InlineKeyboardMarkup kb, LocalDate dateFrom, LocalDate dateTill) {
        LocalDate today = LocalDate.now();
        LocalDate d = dateFrom;
        int i = 1;

        while (!dateTill.equals(d)){
            InlineKeyboardButton button = d.equals(today)
                    ? new InlineKeyboardButton(dateMenuConfig.getToday()).callbackData(CallbackType.DATE + ":" + today)
                    : new InlineKeyboardButton(dateTimeUtil.parseDateToDayString(d)).callbackData(CallbackType.DATE + ":" + d);

            kb.addRow(button);
            d = dateFrom.plusDays(i);
            i++;
        }

        kb.addRow(
                new InlineKeyboardButton(dateMenuConfig.getForward())
                        .callbackData(CallbackType.DATE_FORWARD + ":" + dateTill)
        );

        if (dateFrom.isAfter(today) || !dateFrom.equals(today)) {
            kb.addRow(
                    new InlineKeyboardButton(dateMenuConfig.getBackward())
                            .callbackData(CallbackType.DATE_BACKWARD + ":" + dateFrom)
            );

        }

        kb.addRow(
                new InlineKeyboardButton(dateMenuConfig.getCancel())
                        .callbackData(CallbackType.CANCEL + ":-1")
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
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getPostpone())
                .callbackData("POSTPONE:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getDelete())
                .callbackData("DELETE:" + taskId));
        //TODO: maybe, I will create logic for this, but not now
        //kb.addRow(new InlineKeyboardButton(crudMenuConfig.getEdit())
        //        .callbackData("EDIT:" + taskId));
        kb.addRow(new InlineKeyboardButton(crudMenuConfig.getCancel())
                .callbackData("CANCEL:" + taskId));

        return kb;
    }

    public ReplyKeyboardMarkup menuKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getCreateCommand())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getUncompletedCommand())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getAllCommand())},
                new KeyboardButton[]{new KeyboardButton(mainMenuConfig.getHelpCommand())}
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

    public Keyboard removeKeyboard() {
        return new ReplyKeyboardRemove();
    }
}
