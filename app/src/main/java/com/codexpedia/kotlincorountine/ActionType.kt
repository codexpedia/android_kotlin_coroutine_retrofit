package com.codexpedia.kotlincorountine

enum class ActionType(val type: String) {
    BLOCKING("blocking"),
    BACKGROUND("background"),
    CALLBACKS("callbacks"),
    SUSPEND("suspend"),
    CONCURRENT("concurrent"),
    NOT_CANCELABLE("not cancellable"),
    PROGRESS("progress"),
    CHANNELS("channels"),
    CANCEL("cancel"),
}
