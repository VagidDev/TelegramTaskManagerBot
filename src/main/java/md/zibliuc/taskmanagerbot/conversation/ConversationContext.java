package md.zibliuc.taskmanagerbot.conversation;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConversationContext {
    private Long chatId;
    private ConversationState state = ConversationState.IDLE;

    private String title;
    private LocalDate date;
    private LocalTime time;
}
