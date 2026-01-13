package md.zibliuc.taskmanagerbot.context;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserContext {
    private UserState state = UserState.IDLE;
    private String title;
    private LocalDate date;
}
