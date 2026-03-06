package md.zibliuc.taskmanagerbot.service;

import md.zibliuc.taskmanagerbot.util.formatters.FormatResult;
import md.zibliuc.taskmanagerbot.util.formatters.FormatStatus;
import md.zibliuc.taskmanagerbot.util.formatters.Formatter;
import md.zibliuc.taskmanagerbot.util.formatters.TimeFirstDigitFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeFormatterService {
    private static final Logger LOGGER = LogManager.getLogger(TimeFormatterService.class);
    private final static List<Formatter<String>> TIME_FORMATTERS = List.of(
            new TimeFirstDigitFormatter()
    );

    //TODO: need to be optimized, to much using new strings
    public String format(String time) {
        String formattedString = time;
        for (Formatter<String> formatter : TIME_FORMATTERS) {
            FormatResult<String> formatResult = formatter.format(formattedString);
            if (formatResult.getStatus() == FormatStatus.FAILED) {
                return formattedString;
            }
            formattedString = formatResult.getResult();
        }

        return formattedString;
    }
}
