package md.zibliuc.taskmanagerbot.validators;

public class TimeFormatSimpleValidator implements SimpleValidator<String> {
    private static final String TIME_REGEX = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
    @Override
    public boolean validate(String s) {
        return s.matches(TIME_REGEX);
    }
}
