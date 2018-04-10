package ru.juriasan.util;

import ru.juriasan.domain.Message;
import ru.juriasan.service.MessageSender;

import java.util.List;

import static ru.juriasan.Main.randomInd;

/**
 * A class, which provides simulation of incoming messages.
 * Messages are passed to MessageSender
 * instance, which handles them.
 */
public class MessageSupplier extends Thread {

    private MessageSender messageSender;
    private List<String> topics;

    public MessageSupplier(MessageSender sender, List<String> topics) {
        this.messageSender = sender;
        this.topics = topics;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            while(true) {
                int tr1 = randomInd(topics.size());
                int tr2 = randomInd(topics.size());
                int first =  Math.min(tr1, tr2);
                int last = Math.max(tr1, tr2);
                List<String> topicsMsg =  topics.subList(first, last);
                messageSender.send(new Message(String.format("Msg %d", i), topicsMsg));
                i++;
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            System.out.println("Message supplier quits");
            return;
        }
    }
}
