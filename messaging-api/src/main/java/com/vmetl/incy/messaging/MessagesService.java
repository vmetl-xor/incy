package com.vmetl.incy.messaging;

import java.util.function.Function;

public interface MessagesService {


    void sendMessage(Message message, Function<Object, Object> postAction);
    void sendShutdownMessage();

}
