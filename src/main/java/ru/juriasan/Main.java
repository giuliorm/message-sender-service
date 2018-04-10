package ru.juriasan;

import ru.juriasan.domain.Message;
import ru.juriasan.domain.Subscriber;
import ru.juriasan.service.WebHookMessageSender;
import ru.juriasan.util.MessageSupplier;
import ru.juriasan.util.SubscriberSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static Random random = new Random();

    /**
     * Generate random index within given length.
     *
     * @param length a length
     * @return an index within length
     */
    public static int randomInd(int length) {
        if (length == 0) {
            throw new RuntimeException();
        }
        return Math.abs(random.nextInt() % length);
    }

    public static int min(int firstInd, int secondInd) {
        return firstInd < secondInd ? firstInd : secondInd;
    }

    public static int max(int firstInd, int secondInd) {
        return firstInd > secondInd ? firstInd : secondInd;
    }

    public static  void main(String[] args) {
        WebHookMessageSender sender = new WebHookMessageSender();
        int topicsCount = 10;
        List<String> topics = new ArrayList<>();
        for (int i = 0; i < topicsCount; i++) {
            topics.add(String.format("topic%d",i));
        }

        SubscriberSupplier subscriberSupplier = new SubscriberSupplier(sender, topics);
        MessageSupplier messageSupplier = new MessageSupplier(sender, topics);

        messageSupplier.start();
        subscriberSupplier.start();
    }
 }