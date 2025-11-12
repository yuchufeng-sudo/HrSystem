package com.ys.hr.mqtt;

import com.ys.common.core.utils.uuid.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * MQTT tool class operations
 *
 * @author Mr.Qu
 * @since v1.1.0 2020-01-10
 */
@Slf4j
@Component
public class MQTTConnect {
    // mqtt server address and port number
    private String HOST = "tcp://xz-ai.info:1883";
    private final String clientId = IdUtils.fastSimpleUUID();
    private final String userName = "test";
    private final String passWord = "test1234";
    private MqttClient mqttClient;

    /**
     * Client connects to the mqtt server
     *
     * @param userName      user name
     * @param passWord      password
     * @param mqttCallback  callback function
     **/
    public void setMqttClient(String userName, String passWord, MqttCallback mqttCallback) throws MqttException {
        MqttConnectOptions options = mqttConnectOptions(userName, passWord);
        if (mqttCallback == null) {
            mqttClient.setCallback(new Callback());
        } else {
            mqttClient.setCallback(mqttCallback);
        }
        mqttClient.connect(options);
    }

    /**
     * MQTT connection parameter settings
     */
    private MqttConnectOptions mqttConnectOptions(String userName, String passWord) throws MqttException {
        mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // default: 30
        options.setConnectionTimeout(10);
        // default: false
        options.setAutomaticReconnect(true);
        // default: true
        options.setCleanSession(false);
        // default: 60
        //options.setKeepAliveInterval(20);
        return options;
    }

    /**
     * Close the MQTT connection
     */
    public void close() throws MqttException {
        mqttClient.disconnect();
        mqttClient.close();
    }

    /**
     * Publish a message to a topic, default qos: 1
     *
     * @param topic: topic to publish to
     * @param msg: message to publish
     */
    public void pub(String topic, String msg) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        //mqttMessage.setQos(2);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * Publish a message to a topic
     *
     * @param topic: topic to publish to
     * @param msg: message to publish
     * @param qos: message quality, Qos: 0, 1, 2
     */
    public void pub(String topic, String msg, int qos) throws MqttException {
        System.out.println("Publish message: " + msg);
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * Subscribe to a topic, this method uses the default Qos level: 1
     *
     * @param topic topic
     */
    public void sub(String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    /**
     * Subscribe to a topic with Qos
     *
     * @param topic topic to subscribe to
     * @param qos message quality: 0, 1, 2
     */
    public void sub(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
    }

    /**
     * Attendance data acquisition
     */
    public void getClockRecord(Date startTime,Date endTime,String sn) throws MqttException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MQTTConnect mqttConnect = new MQTTConnect();
        Callback callback = new Callback();
        mqttConnect.setMqttClient(userName, passWord, callback);

        mqttConnect.sub("/person_f_upload/"+sn);
        mqttConnect.pub("/person_f_down/"+sn," {\n" +
                "\"cmd\":\"F1getTimeRecord\",\n" +
                "\"token\":\"xxx\",\n" +
                "\"sn\":\""+sn+"\",\n" +
                "\"page\": 1,\n" +
                "\"startTime\":\""+dateFormat.format(startTime)+"\",\n" +
                "\"endTime\":\""+dateFormat.format(endTime)+"\"\n" +
                "}",1);
        mqttConnect.pub("/person_f_down/"+sn," {\n" +
                "\"cmd\":\"F1getTimeRecord\",\n" +
                "\"token\":\"xxx\",\n" +
                "\"sn\":\""+sn+"\",\n" +
                "\"page\": 2,\n" +
                "\"startTime\":\""+dateFormat.format(startTime)+"\",\n" +
                "\"endTime\":\""+dateFormat.format(endTime)+"\"\n" +
                "}",1);
        mqttConnect.pub("/person_f_down/"+sn," {\n" +
                "\"cmd\":\"F1getTimeRecord\",\n" +
                "\"token\":\"xxx\",\n" +
                "\"sn\":\""+sn+"\",\n" +
                "\"page\": 3,\n" +
                "\"startTime\":\""+dateFormat.format(startTime)+"\",\n" +
                "\"endTime\":\""+dateFormat.format(endTime)+"\"\n" +
                "}",1);
    }
    /**
     * Remote door opening acquisition
     */
    public void openDoor(String sn) throws MqttException {
        MQTTConnect mqttConnect = new MQTTConnect();
        Callback callback = new Callback();
        mqttConnect.setMqttClient(userName, passWord, callback);
        mqttConnect.sub("/person_f_upload/"+ sn);
        mqttConnect.pub("/person_f_down/"+sn,"{\n" +
                "\"cmd\":\"F1openDoor\",\n" +
                "\"token\":\"xxx\",\n" +
                "\"sn\":\""+sn+"\"\n" +
                "}",1);
        System.out.println(callback);
    }


    /**
     * Take a PhotoGet
     */
    public void photograph(String sn) throws MqttException {
        MQTTConnect mqttConnect = new MQTTConnect();
        Callback callback = new Callback();
        mqttConnect.setMqttClient(userName, passWord, callback);
        mqttConnect.sub("/person_f_upload/"+ sn);
        mqttConnect.pub("/person_f_down/"+ sn,"{\n" +
                "\"cmd\":\"F1photograph\",\n" +
                "\"token\":\"xxx\",\n" +
                "\"sn\":\""+sn+"\"\n" +
                "}",1);
    }
}
