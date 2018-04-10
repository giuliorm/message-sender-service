package ru.juriasan.util;

import ru.juriasan.domain.Subscriber;
import ru.juriasan.service.MessageSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.juriasan.Main.randomInd;

/**
 * A class, which simulates subscriber behavior.
 * Subscribers subscribe and unsubscribe to different topics randomly.
 */
public class SubscriberSupplier extends Thread {
    private MessageSender messageSender;
    private List<String> topics;

    public SubscriberSupplier(MessageSender sender, List<String> topics) {
        this.messageSender = sender;
        this.topics = topics;
    }
    @Override
    public void run() {

        List<Subscriber> subscribers = new ArrayList<>();
        int subscribersCount = 5;
        for(int i = 0; i < subscribersCount; i++) {
            subscribers.add(new Subscriber(String.format("Subscriber %d", i)));
        }
        ExecutorService subscriberService = Executors.newFixedThreadPool(subscribersCount);
        try {
            boolean subscribe = true;
            while(true) {
                int tr1 = randomInd(subscribersCount);
                int tr2 = randomInd(subscribersCount);
                int first = Math.min(tr1, tr2);
                int last = Math.max(tr1, tr2);
                int operation = randomInd(2);
                int topicIndex = randomInd(topics.size());

                for (int j = first; j < last; j++) {
                    Subscriber s = subscribers.get(j);
                    subscriberService.submit(() -> {
                        String topic = topics.get(topicIndex);
                        if (operation == 0) {
                            messageSender.subscribe(s, topic);
                        }
                        else {
                            messageSender.unsubscribe(s, topic);
                        }
                    });
                    Thread.sleep(2000);
                }
            }
        }
        catch (InterruptedException ex) {
            System.out.println("Subscribers thread is exiting");
        }
    }
}
