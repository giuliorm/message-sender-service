package ru.juriasan.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Message {

    private List<String> topics;
    private String message;

    public Message() {

    }

    public Message(String message, List<String> topics) {
        this.topics = topics;
        this.message = message;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getTopics() {
        return Collections.unmodifiableList(topics);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String topicsStr = topics.stream().collect(Collectors.joining(","));
        return String.format("%s(%s)", this.message, topicsStr );
    }
}
