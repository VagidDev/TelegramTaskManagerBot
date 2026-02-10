package md.zibliuc.taskmanagerbot.dto;

import md.zibliuc.taskmanagerbot.callback.CallbackType;

import java.time.LocalDate;

public record CallbackData(
        CallbackType type,
        String payload
) {
    public Long asLong() {
        return payload == null ? null : Long.valueOf(payload);
    }

    public LocalDate asDate() {
        return "TODAY".equals(payload) ?
                LocalDate.now() :
                LocalDate.parse(payload);
    }
}
