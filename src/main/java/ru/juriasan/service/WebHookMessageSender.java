package ru.juriasan.service;

import org.apache.log4j.Logger;
import ru.juriasan.domain.Subscriber;

import java.util.Optional;

public class WebHookMessageSender extends AbstractMessageSender {

    private static final Logger logger = Logger.getLogger(WebHookMessageSender.class);
    private static final int BAD_REQUEST = 400;
    private static final int OK = 200;

    @Override
    public int send(Subscriber subscriber, String message) {
        return Optional.ofNullable(subscriber)
            .map(Subscriber::getUrl)
            .map(url -> Optional.ofNullable(message)
                .map(msg -> {
                    logger.info(String.format("Message %s -> %s",
                            msg, url));
                    return OK;
                })
                .orElseGet(() -> {
                    logger.info(String.format("Cannot send message to subscriber %s: " +
                            "Message is null", url));
                    return BAD_REQUEST;
                }))
            .orElseGet(() -> {
                logger.info("Cannot send message to subscriber: subscriber is null or url is null");
                return BAD_REQUEST;
            });
    }
}
