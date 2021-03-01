package ru.itis.jlab.Consumer.direct;

import com.rabbitmq.client.*;
import ru.itis.jlab.model.CompanyMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;

public class ConsumerCompanyMessage {
    private final static String EXCHANGE_NAME = "messageForCompany";
    private final static String EXCHANGE_TYPE = "direct";
    private final static String packageName = "companies";
    private static String companyFileName;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(3);

            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE);

            String queue = channel.queueDeclare().getQueue();

            channel.queueBind(queue, EXCHANGE_NAME, "");

            DeliverCallback deliverCallback = ConsumerCompanyMessage::handle;

            channel.basicConsume(queue, false, deliverCallback, consumerTag -> {});

        } catch (IOException | TimeoutException e) {
            //TODO: написать reject
            throw new IllegalArgumentException(e);
        }
    }

    private static void handle(String consumerTag, Delivery message) {
        String json = new String(message.getBody());
//            ObjectMapper mapper = new ObjectMapper();
//            CompanyMessage companyMessage = mapper.readValue(json, CompanyMessage.class);
        sendMessage(new CompanyMessage("файл создан", json));

    }

    private static void sendMessage(CompanyMessage companyMessage) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(packageName + "/" + companyMessage.getCompanyName()));
            pw.println(companyMessage.getMessage());
            for (int i = 0; i < 10; i++) {
                pw.print("-");
            }
            pw.close();
            System.out.println("все сделанно");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

}
