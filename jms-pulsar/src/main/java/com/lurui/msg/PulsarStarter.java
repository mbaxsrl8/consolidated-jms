package com.lurui.msg;

import com.datastax.oss.pulsar.jms.PulsarConnectionFactory;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import java.util.HashMap;
import java.util.Map;

public class PulsarStarter {
    public static void main(String[] args) {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("webServiceUrl", "http://localhost:8080");
        configuration.put("brokerServiceUrl", "pulsar://localhost:6650");
        PulsarConnectionFactory factory = new PulsarConnectionFactory(configuration);

        try (JMSContext context = factory.createContext()) {
            Destination destination = context.createQueue("persistent://public/default/test");
            context.createProducer().send(destination, "text");
            try (JMSConsumer consumer = context.createConsumer(destination)) {
                String message = consumer.receiveBody(String.class);
                System.out.println(message);
            }
        }
    }
}
