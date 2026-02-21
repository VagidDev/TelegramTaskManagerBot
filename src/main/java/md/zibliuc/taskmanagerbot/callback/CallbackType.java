package md.zibliuc.taskmanagerbot.callback;

public enum CallbackType {
    TASK,
    //Task actions
    COMPLETE,
    POSTPONE,
    DELETE,
    EDIT,
    //Date callbacks
    DATE,
    DATE_FORWARD,
    DATE_BACKWARD,

    CANCEL,
    // Like exception, but without exception
    UNDEFINED
}
