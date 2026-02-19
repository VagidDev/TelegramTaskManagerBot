package md.zibliuc.taskmanagerbot.callback;

public enum CallbackType {
    TASK,
    //ACTION,
    COMPLETE, DELETE, EDIT, POSTPONE,
    DATE,
    CANCEL,
    // Like exception, but without exception
    UNDEFINED
}
