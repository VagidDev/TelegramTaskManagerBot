package md.zibliuc.taskmanagerbot.util.formatters;

public interface Formatter<T> {
    FormatResult<T> format(T t);
}
