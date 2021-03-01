package ru.itis.jlab.Consumer.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import ru.itis.jlab.model.AnyDataForPdf;
import ru.itis.jlab.utils.PdfWorker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class ConsumerPdfMidPriority {
    private final static String EXCHANGE_NAME = "pdf";
    private final static String EXCHANGE_TYPE = "topic";
    private final static String ROUTING_KEY = "Mid.*";
    private final static String packagePdf = "pdfs";
    private final static String packageTemplate = "templates";
    private static String pdfFileName;

    public static void main(String[] args) {
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
            channel.queueBind(queue, EXCHANGE_NAME, ROUTING_KEY);

            DeliverCallback deliverCallback = ConsumerPdfMidPriority::handle;
//            Consumer consumerTag = new Consumer() {
//                @Override
//                public void handleConsumeOk(String s) {
//
//                }
//
//                @Override
//                public void handleCancelOk(String s) {
//
//                }
//
//                @Override
//                public void handleCancel(String s) throws IOException {
//
//                }
//
//                @Override
//                public void handleShutdownSignal(String s, ShutdownSignalException e) {
//
//                }
//
//                @Override
//                public void handleRecoverOk(String s) {
//
//                }
//
//                @Override
//                public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
//
//                }
//            };
            channel.basicConsume(queue, false, deliverCallback, consumerTag -> { });
        } catch (IOException | TimeoutException e) {
            //TODO: написать reject
            throw new IllegalArgumentException(e);
        }
    }

    private static void handle(String consumerTag, Delivery message) {
        try {
            String json = new String(message.getBody());

            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, HashMap.class);
            AnyDataForPdf data = new AnyDataForPdf(map);
            createPdf(data);
        } catch (JsonProcessingException e) {
            System.err.println("FAILED");
        }

    }

    //TODO: сделать нормальное создание pdf по шаблону
    private static void createPdf(AnyDataForPdf data) {
        PdfWorker pdfWorker = new PdfWorker();
        Map<String, Object> dataMap = data.getDataMap();
        String templateFileName = (String) dataMap.get("template");
        String templatePath = packageTemplate + "." + templateFileName;
        dataMap.remove("template");
        pdfWorker.savePdf(dataMap, templatePath, packagePdf);
    }
}
