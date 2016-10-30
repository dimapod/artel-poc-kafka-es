package com.artel.poc.acq;

import com.artel.poc.acq.acquisition.AmqpListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class RabbitConfig {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${AIRVANTAGE_AMQP_USE_SSL:false}")
    private boolean useSSL;

    @Value("${AIRVANTAGE_AMQP_HOST:localhost}")
    private String amqpHost;

    @Value("${AIRVANTAGE_AMQP_USERNAME:xebia}")
    private String username;

    @Value("${AIRVANTAGE_AMQP_PASSWORD:xebia2015}")
    private String password;

    @Value("${AIRVANTAGE_AMQP_VIRTUALHOST:/}")
    private String virtualHost;

    @Value("${AIRVANTAGE_AMQP_QUEUENAME:airvantage-local}")
    private String queueName;

    @Autowired
    @Qualifier("airvantageConnectionFactoryBean")
    private com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private AmqpListener amqpListener;

    @Bean(name = "rabbitConnectionFactory")
    public ConnectionFactory connectionFactory() throws Exception {
        LOGGER.debug("Trying to connect to AIRVANTAGE queue using the following config");
        LOGGER.debug("Airvantage useSSL=" + useSSL);
        LOGGER.debug("Airvantage amqpHost=" + amqpHost);
        LOGGER.debug("Airvantage queueName=" + queueName);
        LOGGER.debug("Airvantage username=" + username);
        LOGGER.debug("Airvantage password=" + password);
        LOGGER.debug("Airvantage virtualHost=" + virtualHost);

        CachingConnectionFactory factory = new CachingConnectionFactory(rabbitConnectionFactory);

        factory.setHost(amqpHost);
        if (useSSL) {
            factory.setPort(5671);
        } else {
            factory.setPort(5672);
        }
        if (username != null) {
            factory.setUsername(username);
        }
        if (password != null) {
            factory.setPassword(password);
        }
        factory.setVirtualHost(virtualHost);
        return factory;
    }

    @Bean
    public Queue airvantageQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 604800000);
        return new Queue(queueName, true, false, false, arguments);
    }

    @Bean(name = "rabbitMessageListenerContainer")
    public MessageListenerContainer messageListenerContainer(Queue airvantageQueue) throws Exception {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory());
        simpleMessageListenerContainer.addQueues(airvantageQueue);
        simpleMessageListenerContainer.setMessageListener(amqpListener);
        return simpleMessageListenerContainer;
    }

    @Configuration
    protected static class RabbitConnectionFactoryBeanCreator {
        @Value("${amqp.useSSL:true}")
        private boolean useSSL;

        @Bean(name = "airvantageConnectionFactoryBean")
        public RabbitConnectionFactoryBean connectionFactoryBean() {
            RabbitConnectionFactoryBean rabbitConnectionFactoryBean = new RabbitConnectionFactoryBean();
            rabbitConnectionFactoryBean.setUseSSL(useSSL);
            return rabbitConnectionFactoryBean;
        }
    }
}
