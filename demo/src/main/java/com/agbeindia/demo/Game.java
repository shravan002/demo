package com.agbeindia.demo;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Player implements Runnable {
    protected final BlockingQueue<String> sentMessage;
    protected final BlockingQueue<String> receivedMessage;

    /**
     * Kindly note that the integer field has the potential to exceed its maximum value during an extended
     * program execution. Consequently, once it reaches 2147483647, it will wrap around to -2147483648.
     * To address this, you have the option to employ BigInteger or to assess the field against
     * Integer.MAX_VALUE prior to each increment operation.
     */

    //For the sake of simplicity, let's opt for BigInteger.
    private BigInteger totalSentMessage = new BigInteger("0");

    public Player(BlockingQueue<String> sentMessage, BlockingQueue<String> receivedMessage) {
        this.sentMessage = sentMessage;
        this.receivedMessage = receivedMessage;
    }

    @Override
    public void run() {
        while (true) {
            String receivedMessage = doReceive();
            messageReply(receivedMessage);
        }
    }

    protected String doReceive() {
        String receivedMessage;
        try {
            //Dequeuing the message if it's present in the queue, or else patiently await its arrival.
            receivedMessage = this.receivedMessage.take();
        } catch (InterruptedException interrupted) {
            String messageError = String.format("Player [%s] failed to receive message on iteration [%d].", this, totalSentMessage);
            throw new IllegalStateException(messageError, interrupted);
        }
        return receivedMessage;
    }

    protected void messageReply(String receivedMessage) {
        String messageReply = receivedMessage + " " + totalSentMessage;
        try {
            //Dispatch a message if the queue has available space,
            // or remain in wait until there is room for a message to be accommodated.
            sentMessage.put(messageReply);
            System.out.printf("Player's [%s] sent message [%s].%n", this, messageReply);
            totalSentMessage = totalSentMessage.add(BigInteger.ONE);

            // Every player will function correctly even if this delay is not present.
            // Its purpose here is merely to decelerate the console output.
            Thread.sleep(1000);
        } catch (InterruptedException interrupted) {
            String error = String.format("Player's [%s] has failed to send message [%s] on the iteration [%d].", this, messageReply, totalSentMessage);
            throw new IllegalStateException(error);
        }
    }
}

class PlayerFirst extends Player {
    private static final String MESSAGE_INITIATOR = "initiator player";

    public PlayerFirst(BlockingQueue<String> sent, BlockingQueue<String> received) {
        super(sent, received);
    }

    @Override
    public void run() {
        sendInitMessage();
        while (true) {
            String receivedMessage = doReceive();
            messageReply(receivedMessage);
        }
    }

    private void sendInitMessage() {
        try {
            sentMessage.put(MESSAGE_INITIATOR);
            System.out.printf("Player [%s] sent message [%s].%n", this, MESSAGE_INITIATOR);
        } catch (InterruptedException interrupted) {
            String error = String.format("Player [%s] failed to sent message [%s].", this, MESSAGE_INITIATOR);
            throw new IllegalStateException(error, interrupted);
        }
    }
}

public class Game {
    private static final int QUEUE_MAX_MESSAGE = 1;

    /**
     * While a blocking queue might seem unnecessary for a single message, it actually spares us from the intricate task of synchronizing threads.
     * @param args
     */
    public static void main(String[] args) {
        BlockingQueue<String> moveFTSPlayer = new ArrayBlockingQueue<String>(QUEUE_MAX_MESSAGE);
        BlockingQueue<String> moveSTFPlayer = new ArrayBlockingQueue<String>(QUEUE_MAX_MESSAGE);

        //Both players employ the identical queues in a symmetrical manner.
        PlayerFirst firstPlayer = new PlayerFirst(moveFTSPlayer, moveSTFPlayer);
        Player secondPlayer = new Player(moveSTFPlayer, moveFTSPlayer);

        //Kindly be aware that we have the capability to initiate threads in a reversed sequence.
        // However, owing to the presence of blocking queues, the second participant will patiently
        // await receipt of the initialization message from the first participant.
        new Thread(secondPlayer).start();
        new Thread(firstPlayer).start();
    }
}