package ru.juriasan.service;

import org.apache.log4j.Logger;
import ru.juriasan.domain.Message;
import ru.juriasan.domain.Subscriber;

import java.util.Objects;
import java.util.concurrent.*;

public abstract class AbstractMessageSender implements MessageSender {

    private static final Logger logger = Logger.getLogger(AbstractMessageSender.class);
    private static final int MAX_THREAD_POOL_SIZE = 15;
    private static ExecutorService senderService;
    private static ExecutorService topicIteratorService;

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Subscriber>> topics;
    static {
        topicIteratorService = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        senderService =  Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
    }
    public AbstractMessageSender() {
        this.topics = new ConcurrentHashMap<>();
    }

    public void subscribe(Subscriber subscriber, String topicName) {
        if (subscriber == null || topicName == null) {
            logger.info("Cannot subscribe to topic: subscriber or topic name are absent");
            return;
        }
        topics.putIfAbsent(topicName, new ConcurrentLinkedQueue<>());
        ConcurrentLinkedQueue<Subscriber> set = topics.get(topicName);
        if (!set.contains(subscriber)) {
            set.add(subscriber);
            logger.info(String.format("SUBSCRIBED: Subscriber %s to topic %s", subscriber.getUrl(), topicName));
        }
        else {
            logger.info(String.format("Subscriber %s is already on topic %s", subscriber.getUrl(), topicName));
        }
    }

    public void unsubscribe(Subscriber subscriber, String topicName) {
        if (subscriber == null || topicName == null) {
            logger.info("Cannot unsubscribe from topic: subscriber or topic name are absent");
            return;
        }
        ConcurrentLinkedQueue<Subscriber> set = topics.get(topicName);
        if (set != null) {
            if (set.contains(subscriber)) {
                set.remove(subscriber);
                logger.info(String.format("UNSUBSCRIBED: Subscriber %s from topic %s", subscriber.getUrl(), topicName));
            }
            else logger.info(String.format("Subscriber %s is not on topic %s", subscriber.getUrl(), topicName));
        }
    }

    //sends message to all subscribers in all topics mentioned in message
    public void send(Message message) {
        if (message == null || message.getTopics() == null) {
            logger.info("Cannot send message: message is null or topics are absent");
            return;
        }
        message.getTopics()
            .stream()
            .map(topic -> topics.get(topic))
            .filter(Objects::nonNull)
            .forEach(subscribers -> {
                topicIteratorService.submit(() -> {
                    subscribers.forEach(subscriber -> {
                        senderService.submit(() -> {
                            send(subscriber, message.toString());
                        });
                    });
                });
            });
    }
}
