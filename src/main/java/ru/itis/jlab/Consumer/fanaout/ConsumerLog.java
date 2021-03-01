package ru.itis.jlab.Consumer.fanaout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class ConsumerLog {
    private final static String EXCHANGE_NAME = "logs";
    private final static String EXCHANGE_TYPE = "fanout";
    private final static String packageName = "logs";
    private static String logFileName;
    private static int day;

    public static void main(String[] args) {
        createNewLogFile();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(3);

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);
            // создаем временную очередь со случайным названием
            String queue = channel.queueDeclare().getQueue();

            // привязали очередь к EXCHANGE_NAME
            channel.queueBind(queue, EXCHANGE_NAME, "");

            DeliverCallback deliverCallback = (consumerTag, message) -> {
                if (day != LocalDateTime.now().getDayOfYear()) {
                    createNewLogFile();
                }
                String jsonUser = new String(message.getBody());
                PrintWriter pw = new PrintWriter(packageName + "/" + logFileName);
                pw.println();
                pw.println(LocalDateTime.now() + " Новый запрос для очереди со след данными: ");
                pw.println(jsonUser);
                pw.close();
            };

            channel.basicConsume(queue, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void createNewLogFile() {
        day = LocalDateTime.now().getDayOfYear();
        logFileName = "logs about " + day + " day" + ".txt";
        new File(packageName + "." + logFileName);
    }
}
