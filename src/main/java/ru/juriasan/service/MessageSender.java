package ru.juriasan.service;

import ru.juriasan.domain.Message;
import ru.juriasan.domain.Subscriber;

public interface MessageSender {

    void subscribe(Subscriber subscriber, String topicName);
    void unsubscribe(Subscriber subscriber, String topicName);
    //sends message to single subscriber
    int send(Subscriber subscriber, String message);
    //sends message to all subscribers in all topics mentioned in message
    void send(Message message);
}
