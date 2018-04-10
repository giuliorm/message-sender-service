package ru.juriasan.service;

import ru.juriasan.domain.Message;
import ru.juriasan.domain.Subscriber;

/**
 * A common interface, which defines message broker (sender) functionality.
 */
public interface MessageSender {

    /**
     * Given a Subscriber, enables a subscription to specified topic.
     * If topic does not exist, it is created.
     *
     * @param subscriber a subscriber instance
     * @param topicName a topic name
     */
    void subscribe(Subscriber subscriber, String topicName);

    /**
     *  Tries to unsubscribe given subscriber. Does nothing, in case
     *  subscriber is not in the list of subscribers of specified topic.
     *  Does nothing, if the topic does not exist.
     *  '
     * @param subscriber A subscriber
     * @param topicName a topic name.
     */
    void unsubscribe(Subscriber subscriber, String topicName);

    /**
     * A method for sending a message to single subscriber.
     * Should rub asynchroniously.
     *
     * @param subscriber A subscriber, which holds all the necessary information
     *                   to send a message
     * @param message String message content
     * @return Status code, indicating the result of the send operation
     */
    int send(Subscriber subscriber, String message);

    /**
     *  Sends message to all subscribers in all topics mentioned in message.
     *
     * @param message A message instance, contains the list of topics and
     *                string message content.
     */
    void send(Message message);
}
