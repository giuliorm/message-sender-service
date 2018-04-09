package ru.juriasan;

import ru.juriasan.domain.Message;
import ru.juriasan.domain.Subscriber;
import ru.juriasan.service.WebHookMessageSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static Random random = new Random();

    private static int randomInd(int length) {
        return Math.abs(random.nextInt() % length);
    }

    private static int first(int firstInd, int secondInd) {
        return firstInd < secondInd ? firstInd : secondInd;
    }

    private static int last(int firstInd, int secondInd) {
        return firstInd > secondInd ? firstInd : secondInd;
    }

    public static  void main(String[] args) {
        WebHookMessageSender sender = new WebHookMessageSender();
        int topicsCount = 10;
        List<String> topics = new ArrayList<>();
        for (int i = 0; i < topicsCount; i++) {
            topics.add(String.format("topic%d",i));
        }

        Thread subscriberSupplier = new Thread(() -> {
            List<Subscriber> subscribers = new ArrayList<>();
            int subscribersCount = 5;
            for(int i = 0; i < subscribersCount; i++) {
                subscribers.add(new Subscriber(String.format("Subscriber %d", i)));
            }
            Random r = new Random();
            ExecutorService subscriberService = Executors.newFixedThreadPool(subscribersCount);
            try {
                boolean subscribe = true;
                while(true) {
                    int tr1 = randomInd(subscribersCount);
                    int tr2 = randomInd(subscribersCount);
                    int first = first(tr1, tr2);
                    int last = last(tr1, tr2);
                    int operation = randomInd(2);
                    int topicIndex = randomInd(topicsCount);

                    for (int j = first; j < last; j++) {
                        Subscriber s = subscribers.get(j);
                        subscriberService.submit(() -> {
                            String topic = topics.get(topicIndex);
                            if (operation == 0) {
                                sender.subscribe(s, topic);
                            }
                            else {
                                sender.unsubscribe(s, topic);
                            }
                        });
                        Thread.sleep(2000);
                    }
                }
            }
            catch (InterruptedException ex) {
                System.out.println("Subscribers thread is exiting");
                return;
            }
        });

        Thread messageSupplier = new Thread(() -> {
            try {

                Random r = new Random();
                int i = 0;
                while(true) {
                    int tr1 = randomInd(topicsCount);
                    int tr2 = randomInd(topicsCount);
                    int first =  first(tr1, tr2);
                    int last = last(tr1, tr2);
                    List<String> topicsMsg =  topics.subList(first, last);
                    sender.send(new Message(String.format("Msg %d", i), topicsMsg));
                    i++;
                    Thread.sleep(100);
                }
            }
            catch (InterruptedException ex) {
                System.out.println("Message supplier quits");
                return;
            }
        });
        messageSupplier.start();
        subscriberSupplier.start();
    }
 }