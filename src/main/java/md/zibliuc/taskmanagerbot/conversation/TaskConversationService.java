package md.zibliuc.taskmanagerbot.conversation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TaskConversationService {

    public void handle(Long chatId, String text) {

    }

    public void onTitle(Long chatId, String text) {}
    public void onDate(Long chatId, LocalDate date) {}
    public void onTime(Long chatId, LocalTime time) {}

}
