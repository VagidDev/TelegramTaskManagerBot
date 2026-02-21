package md.zibliuc.taskmanagerbot.dto;

import md.zibliuc.taskmanagerbot.callback.CallbackType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record CallbackData(
        CallbackType type,
        String payload
) {
    public Long asLong() throws NumberFormatException {
        return payload == null ? null : Long.valueOf(payload);
    }

    public LocalDate asDate() throws DateTimeParseException {
        return LocalDate.parse(payload);
    }
}
