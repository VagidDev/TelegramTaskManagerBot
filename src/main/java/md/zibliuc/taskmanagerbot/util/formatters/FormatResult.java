package md.zibliuc.taskmanagerbot.util.formatters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormatResult<T> {
    private FormatStatus status;
    private T result;
}

