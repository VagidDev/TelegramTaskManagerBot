package md.zibliuc.taskmanagerbot.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ui.responses.callback")
@RequiredArgsConstructor
@Getter
public class CallbackResponseConfig {
    //date handler
    private final String datePicking;
    private final String datePickingError;
    //select handler
    private final String selectTask;
    private final String selectTaskNotFound;
    private final String selectTaskError;
    //action handler
    private final String complete;
    private final String completeError;
    private final String postpone;
    private final String postponeError;
    private final String delete;
    private final String deleteError;
    private final String edit;
    private final String editError;
    private final String actionError;
    //cancel handler
    private final String cancel;
    private final String undefined;

}
