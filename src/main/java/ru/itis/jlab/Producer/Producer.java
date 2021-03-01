package ru.itis.jlab.Producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {

    private final static String EXCHANGE_NAME_PDF = "pdf";

    private final static String EXCHANGE_NAME_LOG = "logs";

    private final static String EXCHANGE_TYPE_FANOUT = "fanout";

    private final static String EXCHANGE_TYPE_TOPIC = "topic";

    private final static String EXCHANGE_TYPE_DIRECT = "direct";

    private final static String dataPackage = "data";

    private final static String companiesPackage = "data";

    private static ObjectMapper objectMapper = new ObjectMapper();
//    private final static String dataFileName = "data_for_sample_";
//
//    private static int fileCounter = 1;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String dataFileName = sc.nextLine();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(dataPackage + "/" + dataFileName));
                StringBuilder jsonStringBuilder = new StringBuilder();
                readJsonFromFile(jsonStringBuilder, reader);
                String json = jsonStringBuilder.toString();
                ProcessTheLog(connectionFactory, json);
                String templateName = sc.nextLine();
                json = addStringInJson("template", templateName, json);
                ProcessThePdf(connectionFactory, json, "LOW");
                String companyName = sc.nextLine();
                json = addStringInJson("!companyName", companyName, json);
                ProcessTheCompanyMessage(connectionFactory, companyName);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    /*
    data_for_sample_1
    obrazec-prikaz-o-vvedenii-masochnogo-rezhima-stupid-4
    ООО_Сбербанк_пакостин_корпорейтед
    obrazec-prikaz-o-vvedenii-masochnogo-rezhima-stupid-russia-2
    obrazec-prikaz-o-vvedenii-masochnogo-rezhima-stupid-3
    obrazec-prikaz-o-vvedenii-masochnogo-rezhima-short-2
     */
    //TODO: очень тупо, переписать
    private static String addStringInJson(String name, Object obj, String json) {
        try {
            Map map = objectMapper.readValue(json, HashMap.class);
            map.put(name, obj);
            json = objectMapper.writeValueAsString(map);
            return json;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void ProcessTheCompanyMessage(ConnectionFactory connectionFactory, String companyName) {
        try {
            String json = addStringInJson("companyName", companyName, "{}");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare("messageForCompany", EXCHANGE_TYPE_DIRECT);
            channel.basicPublish("messageForCompany", "", null, companyName.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void ProcessThePdf(ConnectionFactory connectionFactory, String json, String priority) {
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME_PDF, EXCHANGE_TYPE_TOPIC);
            channel.basicPublish(EXCHANGE_NAME_PDF, priority + "." + "pdf", null, json.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static void ProcessTheLog(ConnectionFactory connectionFactory, String json) {
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME_LOG, EXCHANGE_TYPE_FANOUT);
            channel.basicPublish(EXCHANGE_NAME_LOG, "", null, json.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new IllegalArgumentException(e);
        }
    }


    private static void readJsonFromFile(StringBuilder stringBuffer, BufferedReader reader) {
        try {
            while (reader.ready()) {
                stringBuffer.append(reader.readLine()).append("\n");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
